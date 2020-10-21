package fr.labard.simplegpstracker

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import fr.labard.simplegpstracker.ui.MainActivityViewModelFactory

class RecordListFragment : Fragment(R.layout.fragment_record_list) {

//    companion object {
//        fun newInstance() = MainFragment()
//    }

    private val viewModel by viewModels<RecordListFragmentViewModel> {
        MainActivityViewModelFactory((requireContext().applicationContext as GPSApplication).appRepository)
    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_record_list, container, false)
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//    }

}