package fr.labard.simplegpstracker.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.labard.simplegpstracker.R
import fr.labard.simplegpstracker.data.local.LocationEntity
import fr.labard.simplegpstracker.data.local.RecordEntity
import fr.labard.simplegpstracker.tracker.TrackerActivity
import fr.labard.simplegpstracker.util.Constants


class RecordListAdapter (val context: Context?) :
    RecyclerView.Adapter<RecordListAdapter.RecordViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mAllRecords: List<RecordEntity>? = null
    private var mAllLocations: List<LocationEntity>? = null

    inner class RecordViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout: LinearLayout = itemView.findViewById(R.id.recyclerview_item_layout)
        val name: TextView = itemView.findViewById(R.id.recyclerview_item_name)
        val creationDate: TextView = itemView.findViewById(R.id.recyclerview_item_creationdate)
        val nbLocations: TextView = itemView.findViewById(R.id.recyclerview_item_nblocations)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view = mInflater.inflate(R.layout.recyclerview_record_list_item, parent, false)
        return RecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        if (mAllRecords != null) {
            val record = mAllRecords!![position]

            holder.name.text = record.name
            holder.creationDate.text = context?.getString(R.string.created)?.format(record.creationDate.toString())
            holder.nbLocations.text = context?.getString(R.string.locations)
                ?.format(mAllLocations?.filter { it.recordId == record.id }?.count().toString())

            holder.layout.setOnClickListener {
                val intent = Intent(context, TrackerActivity::class.java)
                intent.putExtra(Constants.Intent.RECORD_ID_EXTRA, record.id)
                context?.startActivity(intent)
            }
        } else {
            holder.name.text = context?.getString(R.string.no_record)
        }
    }

    fun setRecords(records: List<RecordEntity>?) {
        mAllRecords = records
        notifyDataSetChanged()
    }

    fun setLocations(locations: List<LocationEntity>?) {
        mAllLocations = locations
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (mAllRecords != null) mAllRecords!!.size else 0
    }
}