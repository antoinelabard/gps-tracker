package fr.labard.simplegpstracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.labard.simplegpstracker.model.data.IRepository

class MainActivityViewModel(
    private val appRepository: IRepository
) : ViewModel()

@Suppress("UNCHECKED_CAST")
class MainActivityViewModelFactory (
    private val tasksRepository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (MainActivityViewModel(tasksRepository) as T)
}