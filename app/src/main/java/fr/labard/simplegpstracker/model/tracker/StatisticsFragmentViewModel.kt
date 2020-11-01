package fr.labard.simplegpstracker.model.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.labard.simplegpstracker.model.data.IRepository

class StatisticsFragmentViewModel(
    private val appRepository: IRepository
) : ViewModel() {


}

@Suppress("UNCHECKED_CAST")
class StatisticsFragmentViewModelFactory (
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (StatisticsFragmentViewModel(repository) as T)
}