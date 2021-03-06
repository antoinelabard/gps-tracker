package fr.labard.gpsgpx.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.labard.gpsgpx.GPSApplication
import fr.labard.gpsgpx.R
import kotlinx.android.synthetic.main.fragment_record_list.*

/**
 * RecordListFragment displays the list of all the record stored in the repository in MainActivity.
 */
class RecordListFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: RecordListAdapter

    private val viewModel by viewModels<RecordListFragmentViewModel> {
        RecordListFragmentViewModelFactory(
            requireContext().applicationContext as GPSApplication,
            (requireContext().applicationContext as GPSApplication).appRepository
        )
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

        viewModel.allRecords.observe(viewLifecycleOwner, {
            records -> adapter.setRecords(records ?: listOf())
        })
        viewModel.allLocations.observe(viewLifecycleOwner, {
            locations -> adapter.setLocations(locations)
        })

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity_main_new_record_fab.setOnClickListener {
            viewModel.insertRecord()
            adapter.notifyDataSetChanged()
        }
    }
}