/*
 * Copyright (c) Kuba Szczodrzyński 2019-10-4.
 */

package pl.szczodrzynski.edziennik.api.v2.librus.data.api

import pl.szczodrzynski.edziennik.api.v2.librus.data.DataLibrus
import pl.szczodrzynski.edziennik.api.v2.librus.data.LibrusApi

class LibrusApiEvents(override val data: DataLibrus,
                        val onSuccess: () -> Unit) : LibrusApi(data) {
    companion object {
        const val TAG = "LibrusApiEvents"
    }

    init {
        apiGet(LibrusApiMe.TAG, "") { json ->

            // on error
            data.error(TAG, ERROR_LIBRUS_API_, response, json)

            data.setSyncNext(ENDPOINT_LIBRUS_API_, 2 * DAY)
            onSuccess()
        }
    }
}