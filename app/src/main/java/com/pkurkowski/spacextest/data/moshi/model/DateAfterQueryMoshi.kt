package com.pkurkowski.spacextest.data.moshi.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.text.SimpleDateFormat
import java.util.*

@JsonClass(generateAdapter = true)
class DateAfterQueryMoshi internal constructor(
    val query: DateUtcMoshi
) {
    companion object {

        private val dateFormat: SimpleDateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ROOT)

        fun getFromDate(date: Date) =
            DateAfterQueryMoshi(DateUtcMoshi(GteMoshi(dateFormat.format(date))))
    }
}

@JsonClass(generateAdapter = true)
class DateUtcMoshi internal constructor(
    val date_utc: GteMoshi
)

@JsonClass(generateAdapter = true)
class GteMoshi internal constructor(
    @Json(name = "\$gte") val gte: String
)

