package fr.labard.simplegpstracker

import android.app.Application
import fr.labard.simplegpstracker.model.data.IRepository

class GPSApplication : Application() {

    val appRepository: IRepository
        get() = ServiceLocator.provideAppRepository(this)

    override fun onCreate() {
        super.onCreate()
//        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    }
}