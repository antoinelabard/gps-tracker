package com.example.simplegpstracker.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.simplegpstracker.R
import com.example.simplegpstracker.model.db.record.RecordEntity


class RecordListAdapter (val context: Context?) :
    RecyclerView.Adapter<RecordListAdapter.RecordViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mAllRecords: List<RecordEntity>? = null

    inner class RecordViewHolder (itemView: View) :
        RecyclerView.ViewHolder(itemView){

        val recordItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val itemView: View = mInflater.inflate(R.layout.recyclerview_item, parent, false)
        return RecordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        if (mAllRecords != null) {
            val record = mAllRecords!![position]
            holder.recordItemView.text = record.name
            holder.recordItemView.setOnClickListener {
                val intent = Intent(context, TrackerActivity::class.java)
                intent.putExtra("record_id", record.id)
                context?.startActivity(intent)
            }
        } else {
            holder.recordItemView.text = "No Record"
        }
    }

    fun setRecords(records: List<RecordEntity>?) {
        mAllRecords = records
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (mAllRecords != null) mAllRecords!!.size else 0
    }
}