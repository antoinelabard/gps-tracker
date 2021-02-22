package fr.labard.gpsgpx.main

import android.content.Context
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

/**
 * Adapter used to manage the data shown in the recyclerview of RecordListFragment.
 */
class RecordListAdapter (val context: Context?) :
    RecyclerView.Adapter<RecordListAdapter.RecordViewHolder>() {

    private var records: List<RecordEntity>? = null
    private var locations: List<LocationEntity>? = null

    // contains all the fields which are displayed in the recyclerview item
    inner class RecordViewHolder(
        val binding: RecyclerviewRecordListItemBinding,
//        val listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: RecordEntity, listener: OnItemClickListener) {
            binding.record = record
//            binding.rec {
//                val intent = Intent(context, TrackerActivity::class.java)
//                intent.putExtra(Constants.Intent.RECORD_ID_EXTRA, record?.id)
//                context?.startActivity(intent)
//            }
//            binding.executePendingBindings()
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
        holder.binding.record = record
        holder.binding.nbLocations = locations?.filter { it.recordId == record?.id }?.count()
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

    interface OnItemClickListener {
        fun onItemClick(record: RecordEntity)
    }
}