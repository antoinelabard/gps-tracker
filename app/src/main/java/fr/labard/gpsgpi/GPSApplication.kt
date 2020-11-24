package fr.labard.gpsgpi

import android.app.Application
import fr.labard.gpsgpi.data.IRepository

class GPSApplication : Application() {

    val appRepository: IRepository
        get() = ServiceLocator.provideAppRepository(this)
}