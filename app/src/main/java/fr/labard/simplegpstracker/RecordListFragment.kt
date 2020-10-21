package fr.labard.simplegpstracker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import fr.labard.simplegpstracker.ui.MainActivityViewModelFactory
import fr.labard.simplegpstracker.ui.RecordListAdapter

class RecordListFragment(context: Context) : Fragment(R.layout.fragment_record_list) {

    lateinit var recyclerView: RecyclerView

    lateinit var adapter: RecordListAdapter

    private val viewModel by viewModels<RecordListFragmentViewModel> {
        MainActivityViewModelFactory((requireContext().applicationContext as GPSApplication).appRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.fragment_record_list, container, false)
        recyclerView = view.findViewById(R.id.recyclerview_item_textView)

        return inflater.inflate(R.layout.fragment_record_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = RecordListAdapter(context)
        recyclerView.adapter = adapter
    }
}