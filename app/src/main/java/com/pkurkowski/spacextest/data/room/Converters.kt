package com.pkurkowski.spacextest.data.room

import androidx.room.TypeConverter
import com.pkurkowski.spacextest.domain.DatePrecision

class Converters {

    @TypeConverter
    fun fromTentativeMaxPrecision(precision: DatePrecision): Int {
        return precision.toDatabaseId()
    }

    @TypeConverter
    fun toTentativeMaxPrecision(id: Int): DatePrecision {
        return DatePrecision.values().firstOrNull { it.toDatabaseId() == id }
            ?: DatePrecision.YEAR
    }

    private fun DatePrecision.toDatabaseId() =
        when (this) {
            DatePrecision.QUARTER -> 0
            DatePrecision.HALF -> 1
            DatePrecision.YEAR -> 2
            DatePrecision.MONTH -> 3
            DatePrecision.DAY -> 4
            DatePrecision.HOUR -> 5
        }

}