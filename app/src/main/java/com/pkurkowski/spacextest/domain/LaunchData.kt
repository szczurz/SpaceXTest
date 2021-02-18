package com.pkurkowski.spacextest.domain

import java.util.*

data class LaunchData(
    val id: String,
    val flightNumber: Int,
    val name: String,
    val date: Date,
    val rocketName: String?,
    val rocketType: String?,
    val missionPathUrl: String?,
    val launchStatus: LaunchStatus,
)


sealed class LaunchStatus {
    object Success: LaunchStatus()
    object Fail: LaunchStatus()
    data class Upcoming(val precision: DatePrecision): LaunchStatus()
}

enum class DatePrecision {
    QUARTER,
    HALF,
    YEAR,
    MONTH,
    DAY,
    HOUR
}