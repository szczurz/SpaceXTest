package com.pkurkowski.spacextest.domain

data class CompanyData(
    val name: String,
    val founder: String,
    val founded: Int,
    val employees: Int,
    val launchSites: Int,
    val valuation: Long,
)
