package fr.labard.simplegpstracker

import android.app.Application
import fr.labard.simplegpstracker.data.IRepository

class GPSApplication : Application() {

    val appRepository: IRepository
        get() = ServiceLocator.provideAppRepository(this)
}