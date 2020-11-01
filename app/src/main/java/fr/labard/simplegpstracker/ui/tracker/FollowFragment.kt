package fr.labard.simplegpstracker.ui.tracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import fr.labard.simplegpstracker.R
import fr.labard.simplegpstracker.tracker.FollowFragmentViewModel

class FollowFragment : Fragment() {

    companion object {
        fun newInstance() = FollowFragment()
    }

    private lateinit var fragmentViewModel: FollowFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follow, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragmentViewModel = ViewModelProviders.of(this).get(FollowFragmentViewModel::class.java)
        // TODO: Use the ViewModel
    }

}