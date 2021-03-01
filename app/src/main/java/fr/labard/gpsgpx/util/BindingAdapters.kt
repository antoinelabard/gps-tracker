package fr.labard.gpsgpx.util

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import fr.labard.gpsgpx.data.local.RecordEntity

class BindingAdapters {
    companion object {
        @BindingAdapter("app:recordName")
        fun recordName(view: Toolbar, recordEntity: RecordEntity) {
            view.title = recordEntity.name
        }
    }
}