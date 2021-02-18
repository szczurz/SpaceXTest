package com.pkurkowski.spacextest.data.moshi.model

import com.pkurkowski.spacextest.data.room.entity.LaunchEntity
import com.pkurkowski.spacextest.domain.DatePrecision
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class LaunchDataMoshi(
    @Json(name = "id") val id: String,
    @Json(name = "flight_number") val flightNumber: Int,
    @Json(name = "name") val name: String,
    @Json(name = "date_utc") val date: Date,
    @Json(name = "rocket") val rocketId: String,
    val links: LinksMoshi,
    @Json(name = "upcoming") val upcoming: Boolean,
    @Json(name = "success") val success: Boolean?,
    @Json(name = "date_precision") val datePrecision: DatePrecision,
)



@JsonClass(generateAdapter = true)
data class LinksMoshi(
    val patch: PathMoshi?,
    val wikipedia: String?,
    val article: String?,
    @Json(name = "youtube_id") val youtubeId: String?,
)

@JsonClass(generateAdapter = true)
data class PathMoshi(
    val small: String?,
)

fun LaunchDataMoshi.toLaunchEntity() = LaunchEntity(
    id = this.id,
    flightNumber = this.flightNumber,
    name = this.name,
    timestamp = this.date.time,
    rocketName = null,
    rocketType = null,
    missionPathUrl = this.links.patch?.small,
    upcoming = this.upcoming,
    success = this.success,
    datePrecision = this.datePrecision
)
