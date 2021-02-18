package com.pkurkowski.spacextest.presentation

import com.pkurkowski.spacextest.domain.LaunchData
import com.pkurkowski.spacextest.domain.LaunchStatus
import java.util.*

sealed class FilterData(val order: Order) {
    class All(order: Order) : FilterData(order) {
        override fun reverse() = All(this.order.revert())
    }

    class LaunchYear(val year: Int, order: Order) : FilterData(order) {
        override fun reverse() = LaunchYear(this.year, order.revert())
    }

    class Success(val success: Boolean, order: Order) : FilterData(order) {
        override fun reverse() = Success(this.success, order.revert())
    }

    abstract fun reverse(): FilterData
}

enum class Order {
    ASCENDING, DESCENDING;

    fun revert() = when(this) {
        ASCENDING -> DESCENDING
        DESCENDING -> ASCENDING
    }
}

fun FilterData.process(list: List<LaunchData>): List<LaunchData> {
    return when (this) {
        is FilterData.All -> list
        is FilterData.LaunchYear -> {
            val calendar = Calendar.getInstance()
            list.filter {
                calendar.time = it.date
                calendar.get(Calendar.YEAR) == this.year
            }
        }
        is FilterData.Success -> list.filter { it.launchStatus is LaunchStatus.Success == this.success }
    }.let {
        if (this.order == Order.DESCENDING) it.reversed()
        else it
    }
}