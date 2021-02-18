package com.pkurkowski.spacextest.data.moshi.adapter

import com.pkurkowski.spacextest.domain.DatePrecision
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.*


class DatePrecisionAdapter {

    @ToJson
    fun toJson(precision: DatePrecision): String {
        return precision.name.toLowerCase(Locale.ROOT)
    }

    @FromJson
    fun fromJson(name: String) =
        DatePrecision.values().firstOrNull {
            it.name.toLowerCase(Locale.ROOT) == name
        } ?: DatePrecision.YEAR


}