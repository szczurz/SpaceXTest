package com.pkurkowski.spacextest.application

import android.app.Application
import com.pkurkowski.spacextest.BuildConfig
import com.pkurkowski.spacextest.application.di.applicationModule
import com.pkurkowski.spacextest.application.di.remoteDataSourceModule
import com.pkurkowski.spacextest.application.di.roomDataSourceModule
import com.pkurkowski.spacextest.application.di.schedulerModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class SpaceXTestApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@SpaceXTestApplication)
            // module list
            modules(applicationModule, remoteDataSourceModule, roomDataSourceModule, schedulerModule)
        }
    }

}