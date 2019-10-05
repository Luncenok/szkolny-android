/*
 * Copyright (c) Kuba Szczodrzyński 2019-10-5.
 */

package pl.szczodrzynski.edziennik.api.v2.template.login

import okhttp3.Cookie
import pl.szczodrzynski.edziennik.DAY
import pl.szczodrzynski.edziennik.HOUR
import pl.szczodrzynski.edziennik.api.v2.ERROR_LOGIN_DATA_MISSING
import pl.szczodrzynski.edziennik.api.v2.ERROR_PROFILE_MISSING
import pl.szczodrzynski.edziennik.api.v2.models.ApiError
import pl.szczodrzynski.edziennik.api.v2.template.data.DataTemplate
import pl.szczodrzynski.edziennik.currentTimeUnix

class TemplateLoginApi(val data: DataTemplate, val onSuccess: () -> Unit) {
    companion object {
        private const val TAG = "TemplateLoginApi"
    }

    init { run {
        if (data.profile == null) {
            data.error(ApiError(TAG, ERROR_PROFILE_MISSING))
            return@run
        }

        if (data.isApiLoginValid()) {
            onSuccess()
        }
        else {
            if (/*data.webLogin != null && data.webPassword != null && */true) {
                loginWithCredentials()
            }
            else {
                data.error(ApiError(TAG, ERROR_LOGIN_DATA_MISSING))
            }
        }
    }}

    fun loginWithCredentials() {
        // succeed immediately

        data.apiToken = "ThisIsAVeryLongToken"
        data.apiExpiryTime = currentTimeUnix() + 24 * HOUR
        onSuccess()
    }
}