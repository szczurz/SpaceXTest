package com.pkurkowski.spacextest.application.di

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.dsl.module

val schedulerModule = module {

    single {
        SchedulersProvider(
            Schedulers.io(),
            AndroidSchedulers.mainThread()
        )
    }

}


data class SchedulersProvider(
    val ioScheduler: Scheduler,
    val uiScheduler: Scheduler,
)
