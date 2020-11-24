package fr.labard.gpsgpx

import android.app.Application
import fr.labard.gpsgpx.data.IRepository

class GPSApplication : Application() {

    val appRepository: IRepository
        get() = ServiceLocator.provideAppRepository(this)
}