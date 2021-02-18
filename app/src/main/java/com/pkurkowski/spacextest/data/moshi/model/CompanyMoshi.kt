package com.pkurkowski.spacextest.data.moshi.model

import com.pkurkowski.spacextest.data.room.entity.CompanyEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompanyMoshi(
    val name: String,
    val founder: String,
    val founded: Int,
    val employees: Int,
    @Json(name = "launch_sites") val launchSites: Int,
    val valuation: Long,
    )

// id always 0 to force one company entity
fun CompanyMoshi.toEntity() = CompanyEntity(
    0, name, founder, founded, employees, launchSites, valuation
)