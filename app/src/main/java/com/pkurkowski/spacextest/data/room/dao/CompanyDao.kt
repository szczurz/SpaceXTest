package com.pkurkowski.spacextest.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pkurkowski.spacextest.data.room.entity.CompanyEntity
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface CompanyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCompany(company: CompanyEntity): Completable

    @Query("SELECT * FROM company LIMIT 1")
    fun getCompany(): Maybe<CompanyEntity>

}