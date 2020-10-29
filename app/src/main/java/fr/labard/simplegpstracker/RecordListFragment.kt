package fr.labard.simplegpstracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.labard.simplegpstracker.model.util.Constants
import fr.labard.simplegpstracker.ui.RecordListAdapter
import fr.labard.simplegpstracker.ui.TrackerActivity
import kotlinx.android.synthetic.main.fragment_record_list.*

class RecordListFragment : Fragment() {

    lateinit var recyclerView: RecyclerView

    lateinit var adapter: RecordListAdapter

    private val viewModel by viewModels<RecordListFragmentViewModel> {
        RecordListFragmentViewModelFactory((requireContext().applicationContext as GPSApplication).appRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.fragment_record_list, container, false)
        recyclerView = view.findViewById(R.id.recyclerview_record_list)

        recyclerView.layoutManager = LinearLayoutManager(activity)

        return inflater.inflate(R.layout.fragment_record_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = RecordListAdapter(context)
        recyclerView.adapter = adapter

        activity_main_new_record_fab.setOnClickListener {
//            val newId = viewModel.generateNewId()
            viewModel.insertRecord()//newId)
            adapter.notifyDataSetChanged()
            /*val intent = Intent(activity, TrackerActivity::class.java)
            intent.putExtra(Constants.Intent.RECORD_ID_EXTRA, newId)
            startActivity(intent)*/
        }
    }
}