package com.pkurkowski.spacextest.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pkurkowski.spacextest.domain.CompanyData

// id always 0 to force one company entity
@Entity(tableName = "company")
data class CompanyEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val founder: String,
    val founded: Int,
    val employees: Int,
    val launchSites: Int,
    val valuation: Long,
)

fun CompanyEntity.toCompanyData() = CompanyData(
    name, founder, founded, employees, launchSites, valuation
)