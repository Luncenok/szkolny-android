/*
 * Copyright (c) Kacper Ziubryniewicz 2019-12-25
 */

package pl.szczodrzynski.edziennik.data.api.edziennik.edudziennik.data.web

import android.graphics.Color
import org.jsoup.Jsoup
import pl.szczodrzynski.edziennik.colorFromCssName
import pl.szczodrzynski.edziennik.crc32
import pl.szczodrzynski.edziennik.data.api.Regexes
import pl.szczodrzynski.edziennik.data.api.edziennik.edudziennik.DataEdudziennik
import pl.szczodrzynski.edziennik.data.api.edziennik.edudziennik.ENDPOINT_EDUDZIENNIK_WEB_GRADES
import pl.szczodrzynski.edziennik.data.api.edziennik.edudziennik.data.EdudziennikWeb
import pl.szczodrzynski.edziennik.data.api.models.DataRemoveModel
import pl.szczodrzynski.edziennik.data.db.entity.Grade
import pl.szczodrzynski.edziennik.data.db.entity.Grade.*
import pl.szczodrzynski.edziennik.data.db.entity.Metadata
import pl.szczodrzynski.edziennik.data.db.entity.SYNC_ALWAYS
import pl.szczodrzynski.edziennik.get
import pl.szczodrzynski.edziennik.utils.Utils
import pl.szczodrzynski.edziennik.utils.models.Date

class EdudziennikWebGrades(override val data: DataEdudziennik,
                           val onSuccess: () -> Unit) : EdudziennikWeb(data) {
    companion object {
        private const val TAG = "EdudziennikWebGrades"
    }

    private var semester: Int = 1

    init { data.profile?.also { profile ->
        semester = profile.currentSemester
        getGrades()
    } ?: onSuccess() }

    private fun getGrades() { data.profile?.also { profile ->
        webGet(TAG, data.studentEndpoint + "start/?semester=$semester") { text ->
            val doc = Jsoup.parse(text)

            val subjects = doc.select("#student_grades tbody").firstOrNull()?.children()

            subjects?.forEach { subjectElement ->
                if (subjectElement.id().isBlank()) return@forEach

                val subjectId = subjectElement.id().trim()
                val subjectName = subjectElement.child(0).text().trim()
                val subject = data.getSubject(subjectId, subjectName)

                val gradeType = when {
                    subjectElement.select("#sum").text().isNotBlank() -> TYPE_POINT_SUM
                    else -> TYPE_NORMAL
                }

                val gradeCountToAverage = subjectElement.select("#avg").text().isNotBlank()

                val grades = subjectElement.select(".grade[data-edited]")
                val gradesInfo = subjectElement.select(".grade-tip")

                val gradeValues = if (grades.isNotEmpty()) {
                    subjects.select(".avg-$subjectId .grade-tip > p").first()
                            .text().split('+').map {
                                val split = it.split('*')
                                val value = split[1].trim().toFloatOrNull()
                                val weight = value?.let { split[0].trim().toFloatOrNull() } ?: 0f

                                Pair(value ?: 0f, weight)
                            }
                } else emptyList()

                grades.forEachIndexed { index, gradeElement ->
                    val id = Regexes.EDUDZIENNIK_GRADE_ID.find(gradeElement.attr("href"))?.get(1)?.crc32()
                            ?: return@forEachIndexed
                    val (value, weight) = gradeValues[index]
                    val name = gradeElement.text().trim().let {
                        if (it.contains(',') || it.contains('.')) {
                            val replaced = it.replace(',', '.')
                            val float = replaced.toFloatOrNull()

                            if (float != null && float % 1 == 0f) float.toInt().toString()
                            else it
                        } else it
                    }

                    val info = gradesInfo[index]
                    val fullName = info.child(0).text().trim()
                    val columnName = info.child(4).text().trim()
                    val comment = info.ownText()

                    val description = columnName + if (comment.isNotBlank()) " - $comment" else ""

                    val teacherName = info.child(1).text()
                    val teacher = data.getTeacherByLastFirst(teacherName)

                    val addedDate = info.child(2).text().split(' ').let {
                        val day = it[0].toInt()
                        val month = Utils.monthFromName(it[1])
                        val year = it[2].toInt()

                        Date(year, month, day).inMillis
                    }

                    val color = Regexes.STYLE_CSS_COLOR.find(gradeElement.attr("style"))?.get(1)?.let {
                        if (it.startsWith('#')) Color.parseColor(it)
                        else colorFromCssName(it)
                    } ?: -1

                    val gradeObject = Grade(
                            profileId,
                            id,
                            fullName,
                            color,
                            description,
                            name,
                            value,
                            if (gradeCountToAverage) weight else 0f,
                            semester,
                            teacher.id,
                            subject.id
                    ).apply {
                        type = gradeType
                    }

                    data.gradeList.add(gradeObject)
                    data.metadataList.add(Metadata(
                            profileId,
                            Metadata.TYPE_GRADE,
                            id,
                            profile.empty,
                            profile.empty,
                            addedDate
                    ))
                }

                val proposed = subjectElement.select(".proposal").firstOrNull()?.text()?.trim()

                if (proposed != null && proposed.isNotBlank()) {
                    val proposedGradeObject = Grade(
                            profileId,
                            (-1 * subject.id) - 1,
                            "",
                            -1,
                            "",
                            proposed,
                            proposed.toFloatOrNull() ?: 0f,
                            0f,
                            semester,
                            -1,
                            subject.id
                    ).apply {
                        type = when (semester) {
                            1 -> TYPE_SEMESTER1_PROPOSED
                            else -> TYPE_SEMESTER2_PROPOSED
                        }
                    }

                    data.gradeList.add(proposedGradeObject)
                    data.metadataList.add(Metadata(
                            profileId,
                            Metadata.TYPE_GRADE,
                            proposedGradeObject.id,
                            profile.empty,
                            profile.empty,
                            System.currentTimeMillis()
                    ))
                }

                val final = subjectElement.select(".final").firstOrNull()?.text()?.trim()

                if (final != null && final.isNotBlank()) {
                    val finalGradeObject = Grade(
                            profileId,
                            (-1 * subject.id) - 2,
                            "",
                            -1,
                            "",
                            final,
                            final.toFloatOrNull() ?: 0f,
                            0f,
                            semester,
                            -1,
                            subject.id
                    ).apply {
                        type = when (semester) {
                            1 -> TYPE_SEMESTER1_FINAL
                            else -> TYPE_SEMESTER2_FINAL
                        }
                    }

                    data.gradeList.add(finalGradeObject)
                    data.metadataList.add(Metadata(
                            data.profileId,
                            Metadata.TYPE_GRADE,
                            finalGradeObject.id,
                            profile.empty,
                            profile.empty,
                            System.currentTimeMillis()
                    ))
                }
            }

            if (!subjects.isNullOrEmpty()) {
                data.toRemove.addAll(listOf(
                        TYPE_NORMAL,
                        TYPE_POINT_SUM,
                        TYPE_SEMESTER1_PROPOSED,
                        TYPE_SEMESTER2_PROPOSED,
                        TYPE_SEMESTER1_FINAL,
                        TYPE_SEMESTER2_FINAL
                ).map {
                    DataRemoveModel.Grades.semesterWithType(semester, it)
                })
            }

            if (profile.empty && semester == 2) {
                semester = 1
                getGrades()
            } else {
                data.setSyncNext(ENDPOINT_EDUDZIENNIK_WEB_GRADES, SYNC_ALWAYS)
                onSuccess()
            }
        }
    } ?: onSuccess() }
}
