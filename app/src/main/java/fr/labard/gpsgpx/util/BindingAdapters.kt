package fr.labard.gpsgpx.util

import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.labard.gpsgpx.R
import fr.labard.gpsgpx.data.local.RecordEntity

object BindingAdapters {
    @BindingAdapter("app:recordName")
    @JvmStatic
    fun recordName(view: Toolbar, recordEntity: RecordEntity) {
        view.title = recordEntity.name
    }

    @BindingAdapter("app:recordingDrawable")
    @JvmStatic
    fun recordingDrawable(view: FloatingActionButton, state: String) {
        if (state == Constants.Service.MODE_RECORD) {
            view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_baseline_stop_24))
        } else {
            view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_baseline_fiber_manual_record_24))
        }
    }
}