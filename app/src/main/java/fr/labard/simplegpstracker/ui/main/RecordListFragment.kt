package fr.labard.simplegpstracker.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.labard.simplegpstracker.GPSApplication
import fr.labard.simplegpstracker.R
import fr.labard.simplegpstracker.model.main.RecordListAdapter
import fr.labard.simplegpstracker.model.main.RecordListFragmentViewModel
import fr.labard.simplegpstracker.model.main.RecordListFragmentViewModelFactory
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
        adapter = RecordListAdapter(context)
        recyclerView.adapter = adapter

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.recordsLiveData.observe(viewLifecycleOwner, {
                records -> adapter.setRecords(records)
        })

        activity_main_new_record_fab.setOnClickListener {
            viewModel.insertRecord()
            adapter.notifyDataSetChanged()
        }
    }
}