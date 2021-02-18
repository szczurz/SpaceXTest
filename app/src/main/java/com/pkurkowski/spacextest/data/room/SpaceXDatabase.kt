package com.pkurkowski.spacextest.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pkurkowski.spacextest.data.room.dao.CompanyDao
import com.pkurkowski.spacextest.data.room.dao.LaunchesDao
import com.pkurkowski.spacextest.data.room.entity.CompanyEntity
import com.pkurkowski.spacextest.data.room.entity.LaunchEntity

@Database(
    entities = [LaunchEntity::class, CompanyEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class SpaceXDatabase : RoomDatabase() {
    abstract fun launchesDao(): LaunchesDao
    abstract fun companyDao(): CompanyDao
}