/*
 * Copyright (c) Kuba Szczodrzyński 2019-9-21.
 */

package pl.szczodrzynski.edziennik.api.v2.librus

import android.content.Context
import com.google.gson.JsonObject
import pl.szczodrzynski.edziennik.App
import pl.szczodrzynski.edziennik.api.AppError
import pl.szczodrzynski.edziennik.api.interfaces.ProgressCallback
import pl.szczodrzynski.edziennik.api.v2.*
import pl.szczodrzynski.edziennik.api.v2.librus.data.DataLibrus
import pl.szczodrzynski.edziennik.api.v2.librus.login.LoginLibrus
import pl.szczodrzynski.edziennik.api.v2.librus.login.LoginLibrusPortal
import pl.szczodrzynski.edziennik.datamodels.LoginStore
import pl.szczodrzynski.edziennik.datamodels.Profile
import pl.szczodrzynski.edziennik.utils.Utils.d

class LibrusTest(val app: App) {
    companion object {
        private const val TAG = "LibrusTest"
    }

    val profile = Profile(1, "Profil", "xd", 1).apply {
        //putStudentData("accountLogin", "1234567")

        //putStudentData("accountCode", LIBRUS_JST_DEMO_CODE)
        //putStudentData("accountPin", LIBRUS_JST_DEMO_PIN)

        putStudentData("accountLogin", "1234567")

        putStudentData("accountToken", "token")
        putStudentData("accountTokenTime", 1569523077)
    }
    val loginStore = LoginStore(1, LOGIN_TYPE_LIBRUS, JsonObject().apply {
        addProperty("email", "test@example.com")
        addProperty("password", "zaq1@WSX")

        addProperty("accessToken", "token")
        addProperty("refreshToken", "refresh")
        addProperty("tokenExpiryTime", 1569523077)
    }).also {
        it.mode = LOGIN_MODE_LIBRUS_EMAIL
    }

    fun go() {
        val data = DataLibrus(app, profile, loginStore).apply {
            callback = object : ProgressCallback {
                override fun onProgress(progressStep: Int) {

                }

                override fun onActionStarted(stringResId: Int) {
                    d(TAG, app.getString(stringResId))
                }

                override fun onError(activityContext: Context?, error: AppError) {
                    error.changeIfCodeOther()
                    d(TAG, "Error "+error.getDetails(app))
                }
            }
        }

        LoginLibrus(data, LOGIN_METHOD_LIBRUS_MESSAGES) {
            d(TAG, "Login succeeded.")
            d(TAG, "Profile data: ${data.profile?.studentData?.toString()}")
            d(TAG, "LoginStore data: ${data.loginStore.data}")
        }
    }
}