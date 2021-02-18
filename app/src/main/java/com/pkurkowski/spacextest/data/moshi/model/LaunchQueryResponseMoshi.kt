package com.pkurkowski.spacextest.data.moshi.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LaunchQueryResponseMoshi( val docs: List<LaunchDataMoshi>)