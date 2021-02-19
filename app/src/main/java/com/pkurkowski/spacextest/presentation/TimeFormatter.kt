package com.pkurkowski.spacextest.presentation

import android.content.res.Resources
import com.pkurkowski.spacextest.R
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

object TimeFormatter {

    fun getDateString(date: Date): String = DateFormat.getDateInstance().format(date)
    fun getTimeString(date: Date): String = DateFormat.getTimeInstance().format(date)
    fun getYear(date: Date): Int = Calendar.getInstance().also { it.time = date }.get(Calendar.YEAR)


    fun getSinceFromDate(date: Date, resources: Resources): String {
        val difference =
            TimeUnit.MILLISECONDS.toDays(date.time - Calendar.getInstance().timeInMillis)
        return if (difference > 0) {
            String.format(resources.getString(R.string.launch_item_from), difference)
        } else {
            String.format(
                resources.getString(R.string.launch_item_since),
                difference.absoluteValue
            )
        }
    }
}