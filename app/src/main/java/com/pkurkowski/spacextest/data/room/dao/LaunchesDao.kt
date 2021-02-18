package com.pkurkowski.spacextest.data.room.dao

import androidx.room.*
import com.pkurkowski.spacextest.data.room.entity.LaunchEntity
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface LaunchesDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLaunches(launches: List<LaunchEntity>): Completable

    @Query("SELECT MAX(timestamp) FROM launches WHERE upcoming = 0")
    fun getLatestDoneLaunchTimestamp(): Maybe<Long>

    @Query("SELECT * FROM launches")
    fun getAllLaunches(): Single<List<LaunchEntity>>

}