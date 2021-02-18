package com.pkurkowski.spacextest.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pkurkowski.spacextest.domain.DatePrecision
import com.pkurkowski.spacextest.domain.LaunchData
import com.pkurkowski.spacextest.domain.LaunchStatus
import java.util.*

@Entity(tableName = "launches")
data class LaunchEntity(
    @PrimaryKey val id: String,
    val flightNumber: Int,
    val name: String,
    val timestamp: Long,
    val rocketName: String?,
    val rocketType: String?,
    val missionPathUrl: String?,
    val upcoming: Boolean,
    val success: Boolean?,
    val datePrecision: DatePrecision,
)

fun LaunchEntity.toLaunchData() = LaunchData(
    id = this.id,
    flightNumber = this.flightNumber,
    name = this.name,
    date = Date(this.timestamp),
    rocketName = this.rocketName,
    rocketType = this.rocketType,
    missionPathUrl = this.missionPathUrl,
    launchStatus = when {
        this.upcoming -> LaunchStatus.Upcoming(datePrecision)
        this.success ?: false -> LaunchStatus.Success
        else -> LaunchStatus.Fail
    }

)