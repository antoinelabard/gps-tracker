package fr.labard.gpsgpx.util

import android.content.Context
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.labard.gpsgpx.R
import fr.labard.gpsgpx.data.local.LocationEntity
import fr.labard.gpsgpx.data.local.RecordEntity

object BindingAdapters {
    @BindingAdapter("app:record_name")
    @JvmStatic
    fun recordName(view: Toolbar, recordEntity: RecordEntity) {
        view.title = recordEntity.name
    }

    @BindingAdapter("app:recording_drawable")
    @JvmStatic
    fun recordingDrawable(view: FloatingActionButton, state: String) {
        if (state == Constants.Service.MODE_RECORD) {
            view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_baseline_stop_24))
        } else {
            view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_baseline_fiber_manual_record_24))
        }
    }

    @BindingAdapter("app:following_drawable")
    @JvmStatic
    fun followingDrawable(view: FloatingActionButton, state: String) {
        if (state == Constants.Service.MODE_FOLLOW) {
            view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_baseline_pause_24))
        } else {
            view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_baseline_play_arrow_24))
        }
    }

    @BindingAdapter(value = ["bind:stats_template", "bind:total_distance"], requireAll = false)
    @JvmStatic
    fun totalDistance(view: TextView, template: String, data: List<LocationEntity>) {
        val d = LocationOps.getTotalDistance(data)
        view.text = template.format("${d}m")
    }

    @BindingAdapter(value = ["bind:stats_template", "bind:total_time"], requireAll = false)
    @JvmStatic
    fun totalTime(view: TextView, template: String, data: List<LocationEntity>) {
        val t = LocationOps.getTotalTime(data) / 1000
        val s = t % 60
        val m = t / 60 % 60
        val h = t / 3600
        // the string looks like "00h00m00s"
        view.text =  template.format((if (h != 0L) "${h}h" else "") + (if (m != 0L) "${m}m" else "") + "${s}s")
    }

    @BindingAdapter(value = ["bind:stats_template", "bind:recent_speed"], requireAll = false)
    @JvmStatic
    fun recentSpeed(view: TextView, template: String, data: List<LocationEntity>) {
        val s = LocationOps.getRecentSPeed(data)
        view.text = template.format("${s * 3.6}km/h")
    }

    @BindingAdapter(value = ["bind:stats_template", "bind:average_speed"], requireAll = false)
    @JvmStatic
    fun averageSpeed(view: TextView, template: String, data: List<LocationEntity>) {
        val s = LocationOps.getAverageSpeed(data)
        view.text = template.format("${s * 3.6}km/h")
    }

    @BindingAdapter(value = ["bind:stats_template", "bind:min_speed"], requireAll = false)
    @JvmStatic
    fun minSpeed(view: TextView, template: String, data: List<LocationEntity>) {
        val s = LocationOps.getMinSpeed(data)
        view.text = template.format("${s * 3.6}km/h")
    }

    @BindingAdapter(value = ["bind:stats_template", "bind:max_speed"], requireAll = false)
    @JvmStatic
    fun maxSpeed(view: TextView, template: String, data: List<LocationEntity>) {
        val s = LocationOps.getMaxSpeed(data)
        view.text = template.format("${s * 3.6}km/h")
    }

    @BindingAdapter(value = ["bind:stats_template", "bind:nb_locations"], requireAll = false)
    @JvmStatic
    fun nbLocations(view: TextView, template: String, data: List<LocationEntity>) {
    view.text = template.format(LocationOps.getNbLocations(data))
    }
}