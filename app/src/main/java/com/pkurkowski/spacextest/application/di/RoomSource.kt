package com.pkurkowski.spacextest.application.di

import androidx.room.Room
import com.pkurkowski.spacextest.data.room.SpaceXDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val roomDataSourceModule = module {

    single {
        Room.databaseBuilder(androidApplication(), SpaceXDatabase::class.java, "spaceXDataBase")
            .build()
    }

    single { get<SpaceXDatabase>().launchesDao() }
    single { get<SpaceXDatabase>().companyDao() }
}