package pl.szczodrzynski.edziennik.api.v2.models

import android.util.LongSparseArray
import androidx.core.util.forEach
import androidx.core.util.isNotEmpty
import com.google.gson.JsonObject
import im.wangchao.mhttp.Response
import pl.szczodrzynski.edziennik.App
import pl.szczodrzynski.edziennik.api.AppError
import pl.szczodrzynski.edziennik.api.AppError.*
import pl.szczodrzynski.edziennik.api.interfaces.ProgressCallback
import pl.szczodrzynski.edziennik.api.v2.interfaces.EndpointCallback
import pl.szczodrzynski.edziennik.datamodels.*
import pl.szczodrzynski.edziennik.models.Date
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

open class Data(val app: App, val profile: Profile?, val loginStore: LoginStore) {

    var fakeLogin = false

    /**
     * A callback passed to all [Endpoint]s and [LoginMethod]s
     */
    lateinit var callback: EndpointCallback

    /**
     * A list of [LoginMethod]s *already fulfilled* during this sync.
     *
     * A [LoginMethod] may add elements to this list only after a successful login
     * with that method.
     */
    val loginMethods = mutableListOf<Int>()

    /**
     * A method which may be overridden in child Data* classes.
     *
     * Calling it should populate [loginMethods] with all
     * already available login methods (e.g. a non-expired OAuth token).
     */
    open fun satisfyLoginMethods() {}

    /**
     * A list of Login method IDs that are still pending
     * to run.
     */
    var targetLoginMethodIds = mutableListOf<Int>()
    /**
     * A list of endpoint IDs that are still pending
     * to run.
     */
    var targetEndpointIds = mutableListOf<Int>()

    /**
     * A map of endpoint IDs to JSON objects, specifying their arguments bundle.
     */
    var endpointArgs = mutableMapOf<Int, JsonObject>()

    val teacherList = LongSparseArray<Teacher>()
    val subjectList = LongSparseArray<Subject>()
    val teamList = mutableListOf<Team>()
    val lessonList = mutableListOf<Lesson>()
    val lessonChangeList = mutableListOf<LessonChange>()
    val gradeCategoryList = mutableListOf<GradeCategory>()
    val gradeList = mutableListOf<Grade>()
    val eventList = mutableListOf<Event>()
    val eventTypeList = mutableListOf<EventType>()
    val noticeList = mutableListOf<Notice>()
    val attendanceList = mutableListOf<Attendance>()
    val announcementList = mutableListOf<Announcement>()
    val messageList = mutableListOf<Message>()
    val messageRecipientList = mutableListOf<MessageRecipient>()
    val messageRecipientIgnoreList = mutableListOf<MessageRecipient>()
    val metadataList = mutableListOf<Metadata>()
    val messageMetadataList = mutableListOf<Metadata>()

    private val db by lazy { app.db }

    init {

        clear()

        if (profile != null) {
            db.teacherDao().getAllNow(profile.id).forEach { teacher ->
                teacherList.put(teacher.id, teacher)
            }
            db.subjectDao().getAllNow(profile.id).forEach { subject ->
                subjectList.put(subject.id, subject)
            }
        }

        /*val teacher = teachers.byNameFirstLast("Jan Kowalski") ?: Teacher(1, 1, "", "").let {
            teachers.add(it)
        }*/

    }

    fun clear() {
        loginMethods.clear()

        teacherList.clear()
        subjectList.clear()
        teamList.clear()
        lessonList.clear()
        lessonChangeList.clear()
        gradeCategoryList.clear()
        gradeList.clear()
        eventTypeList.clear()
        noticeList.clear()
        attendanceList.clear()
        announcementList.clear()
        messageList.clear()
        messageRecipientList.clear()
        messageRecipientIgnoreList.clear()
        metadataList.clear()
        messageMetadataList.clear()
    }

    fun saveData() {
        if (profile == null)
            return

        db.profileDao().add(profile)
        db.loginStoreDao().add(loginStore)

        if (teacherList.isNotEmpty()) {
            val tempList: ArrayList<Teacher> = ArrayList()
            teacherList.forEach { _, teacher ->
                tempList.add(teacher)
            }
            db.teacherDao().addAll(tempList)
        }
        if (subjectList.isNotEmpty()) {
            val tempList: ArrayList<Subject> = ArrayList()
            subjectList.forEach { _, subject ->
                tempList.add(subject)
            }
            db.subjectDao().addAll(tempList)
        }
        if (teamList.isNotEmpty())
            db.teamDao().addAll(teamList)
        if (lessonList.isNotEmpty()) {
            db.lessonDao().clear(profile.id)
            db.lessonDao().addAll(lessonList)
        }
        if (lessonChangeList.isNotEmpty())
            db.lessonChangeDao().addAll(lessonChangeList)
        if (gradeCategoryList.isNotEmpty())
            db.gradeCategoryDao().addAll(gradeCategoryList)
        if (gradeList.isNotEmpty()) {
            db.gradeDao().clear(profile.id)
            db.gradeDao().addAll(gradeList)
        }
        if (eventList.isNotEmpty()) {
            db.eventDao().removeFuture(profile.id, Date.getToday())
            db.eventDao().addAll(eventList)
        }
        if (eventTypeList.isNotEmpty())
            db.eventTypeDao().addAll(eventTypeList)
        if (noticeList.isNotEmpty()) {
            db.noticeDao().clear(profile.id)
            db.noticeDao().addAll(noticeList)
        }
        if (attendanceList.isNotEmpty())
            db.attendanceDao().addAll(attendanceList)
        if (announcementList.isNotEmpty())
            db.announcementDao().addAll(announcementList)
        if (messageList.isNotEmpty())
            db.messageDao().addAllIgnore(messageList)
        if (messageRecipientList.isNotEmpty())
            db.messageRecipientDao().addAll(messageRecipientList)
        if (messageRecipientIgnoreList.isNotEmpty())
            db.messageRecipientDao().addAllIgnore(messageRecipientIgnoreList)
        if (metadataList.isNotEmpty())
            db.metadataDao().addAllIgnore(metadataList)
        if (messageMetadataList.isNotEmpty())
            db.metadataDao().setSeen(messageMetadataList)
    }

    fun error(tag: String, errorCode: Int, response: Response? = null, throwable: Throwable? = null, apiResponse: JsonObject? = null) {
        var code = when (throwable) {
            is UnknownHostException, is SSLException, is InterruptedIOException -> CODE_NO_INTERNET
            is SocketTimeoutException -> CODE_TIMEOUT
            else -> when (response?.code()) {
                400, 401, 424, 500, 503, 404 -> CODE_MAINTENANCE
                else -> errorCode
            }
        }
        callback.onError(ApiError(tag, code).apply { profileId = profile?.id ?: -1 }.withResponse(response).withThrowable(throwable).withApiResponse(apiResponse))
    }
    fun error(tag: String, errorCode: Int, response: Response? = null, apiResponse: String? = null) {
        var code = when (null) {
            is UnknownHostException, is SSLException, is InterruptedIOException -> CODE_NO_INTERNET
            is SocketTimeoutException -> CODE_TIMEOUT
            else -> when (response?.code()) {
                400, 401, 424, 500, 503, 404 -> CODE_MAINTENANCE
                else -> errorCode
            }
        }
        callback.onError(ApiError(tag, code).apply { profileId = profile?.id ?: -1 }.withResponse(response).withApiResponse(apiResponse))
    }
    fun error(apiError: ApiError) {
        callback.onError(apiError)
    }
    fun progress(step: Int) {
        callback.onProgress(step)
    }
    fun startProgress(stringRes: Int) {
        callback.onStartProgress(stringRes)
    }
}