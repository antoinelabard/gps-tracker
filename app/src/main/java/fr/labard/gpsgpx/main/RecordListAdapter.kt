package fr.labard.gpsgpx.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.labard.gpsgpx.R
import fr.labard.gpsgpx.data.local.LocationEntity
import fr.labard.gpsgpx.data.local.RecordEntity
import fr.labard.gpsgpx.tracker.TrackerActivity
import fr.labard.gpsgpx.util.Constants

/**
 * Adapter used to manage the data shown in the recyclerview of RecordListFragment.
 */
class RecordListAdapter (val context: Context?) :
    RecyclerView.Adapter<RecordListAdapter.RecordViewHolder>() {

    private var records: List<RecordEntity>? = null
    private var locations: List<LocationEntity>? = null

    // contains all the fields which are displayed in the recyclerview item
    inner class RecordViewHolder (adapterBinding: AdapterBinding) : RecyclerView.ViewHolder(adapterBinding.root) {
        adapterBinding.recyclerview_item_layout.setOnClickListener {
            val intent = Intent(context, TrackerActivity::class.java)
            intent.putExtra(Constants.Intent.RECORD_ID_EXTRA, record.id)
            context?.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val adapterBinding: AdapterBinding = AdapterBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
        )
        return RecordViewHolder(adapterBinding)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val record = records?.get(position)
        holder.adapterBinding.setRecord(record)
        holder.adapterBinding.setLocations(locations?.filter { it.recordId == record?.id }?.count().toString())

        holder.adapterBinding.executePendingBindings()

//                ?.format(locations?.filter { it.recordId == record.id }?.count().toString())

//            holder.layout.setOnClickListener {
//                val intent = Intent(context, TrackerActivity::class.java)
//                intent.putExtra(Constants.Intent.RECORD_ID_EXTRA, record.id)
//                context?.startActivity(intent)
//            }
    }

    fun setRecords(records: List<RecordEntity>?) {
        this.records = records
        notifyDataSetChanged()
    }

    fun setLocations(locations: List<LocationEntity>?) {
        this.locations = locations
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (records != null) records!!.size else 0
    }
}