package fr.labard.gpsgpx.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.labard.gpsgpx.GPSApplication
import fr.labard.gpsgpx.R
import fr.labard.gpsgpx.databinding.FragmentRecordListBinding

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
    ): View {

        val binding: FragmentRecordListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_record_list, container, false)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        val view = binding.root

        recyclerView = view.findViewById(R.id.recyclerview_record_list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = RecordListAdapter(context)
        recyclerView.adapter = adapter

        viewModel.allRecords.observe(viewLifecycleOwner, {
            records -> adapter.setRecords(records ?: listOf())
            adapter.notifyDataSetChanged()
        })
        viewModel.allLocations.observe(viewLifecycleOwner, {
            locations -> adapter.setLocations(locations)
            adapter.notifyDataSetChanged()
        })

        return view
    }
}