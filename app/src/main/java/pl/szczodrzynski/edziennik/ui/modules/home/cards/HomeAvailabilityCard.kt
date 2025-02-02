/*
 * Copyright (c) Kuba Szczodrzyński 2020-9-3.
 */

package pl.szczodrzynski.edziennik.ui.modules.home.cards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.core.view.plusAssign
import androidx.core.view.setMargins
import coil.load
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import pl.szczodrzynski.edziennik.*
import pl.szczodrzynski.edziennik.data.db.entity.Profile
import pl.szczodrzynski.edziennik.databinding.CardHomeAvailabilityBinding
import pl.szczodrzynski.edziennik.sync.UpdateDownloaderService
import pl.szczodrzynski.edziennik.ui.dialogs.RegisterUnavailableDialog
import pl.szczodrzynski.edziennik.ui.dialogs.UpdateAvailableDialog
import pl.szczodrzynski.edziennik.ui.modules.home.HomeCard
import pl.szczodrzynski.edziennik.ui.modules.home.HomeCardAdapter
import pl.szczodrzynski.edziennik.ui.modules.home.HomeFragment
import kotlin.coroutines.CoroutineContext

class HomeAvailabilityCard(
        override val id: Int,
        val app: App,
        val activity: MainActivity,
        val fragment: HomeFragment,
        val profile: Profile
) : HomeCard, CoroutineScope {
    companion object {
        private const val TAG = "HomeAvailabilityCard"
    }

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun bind(position: Int, holder: HomeCardAdapter.ViewHolder) {
        holder.root.removeAllViews()
        val b = CardHomeAvailabilityBinding.inflate(LayoutInflater.from(holder.root.context))
        b.root.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            setMargins(8.dp)
        }
        holder.root += b.root

        val status = app.config.sync.registerAvailability[profile.registerName]
        val update = app.config.update

        if (update == null && status == null)
            return

        var onInfoClick = { _: View -> }

        if (status != null && !status.available && status.userMessage != null) {
            b.homeAvailabilityTitle.text = HtmlCompat.fromHtml(status.userMessage.title, HtmlCompat.FROM_HTML_MODE_LEGACY)
            b.homeAvailabilityText.text = HtmlCompat.fromHtml(status.userMessage.contentShort, HtmlCompat.FROM_HTML_MODE_LEGACY)
            b.homeAvailabilityUpdate.isVisible = false
            b.homeAvailabilityIcon.setImageResource(R.drawable.ic_sync)
            if (status.userMessage.icon != null)
                b.homeAvailabilityIcon.load(status.userMessage.icon)
            onInfoClick = {
                RegisterUnavailableDialog(activity, status)
            }
        }
        else if (update != null && update.versionCode > BuildConfig.VERSION_CODE) {
            b.homeAvailabilityTitle.setText(R.string.home_availability_title)
            b.homeAvailabilityText.setText(R.string.home_availability_text, update.versionName)
            b.homeAvailabilityUpdate.isVisible = true
            b.homeAvailabilityIcon.setImageResource(R.drawable.ic_update)
            onInfoClick = {
                UpdateAvailableDialog(activity, update)
            }
        }

        b.homeAvailabilityUpdate.onClick {
            if (update == null)
                return@onClick
            activity.startService(Intent(app, UpdateDownloaderService::class.java))
        }

        b.homeAvailabilityInfo.onClick(onInfoClick)
        holder.root.onClick(onInfoClick)
    }

    override fun unbind(position: Int, holder: HomeCardAdapter.ViewHolder) = Unit
}
