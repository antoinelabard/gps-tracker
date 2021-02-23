package fr.labard.gpsgpx.main

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.labard.gpsgpx.data.local.LocationEntity
import fr.labard.gpsgpx.data.local.RecordEntity
import fr.labard.gpsgpx.databinding.RecyclerviewRecordListItemBinding
import fr.labard.gpsgpx.databinding.RecyclerviewRecordListItemBindingImpl
import fr.labard.gpsgpx.tracker.TrackerActivity
import fr.labard.gpsgpx.util.Constants
import android.widget.Toast
import android.view.View

/**
 * Adapter used to manage the data shown in the recyclerview of RecordListFragment.
 */
class RecordListAdapter (val context: Context?) :
    RecyclerView.Adapter<RecordListAdapter.RecordViewHolder>() {

    private var records: List<RecordEntity>? = null
    private var locations: List<LocationEntity>? = null

    inner class RecordViewHolder(
        val binding: RecyclerviewRecordListItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: RecordEntity, nbLocations: Int) {
            binding.record = record
            binding.nbLocations = nbLocations
            binding.root.setOnClickListener {
                val intent = Intent(context, TrackerActivity::class.java)
                intent.putExtra(Constants.Intent.RECORD_ID_EXTRA, record.id)
                context?.startActivity(intent)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val binding = RecyclerviewRecordListItemBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
        )
        return RecordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val record = records?.get(position)
        val nbLocations = locations?.filter { it.recordId == record?.id }?.count() ?: 0
        record?.let { holder.bind(
            it,
            nbLocations
        ) }
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