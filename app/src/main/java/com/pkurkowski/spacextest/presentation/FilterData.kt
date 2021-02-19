package com.pkurkowski.spacextest.presentation

import android.content.res.Resources
import android.os.Parcelable
import com.pkurkowski.spacextest.R
import com.pkurkowski.spacextest.domain.LaunchData
import com.pkurkowski.spacextest.domain.LaunchStatus
import kotlinx.parcelize.Parcelize

sealed class FilterData: Parcelable {

    @Parcelize
    class All(override val order: Order) : FilterData() {
        override fun reverse() = All(this.order.revert())
    }

    @Parcelize
    class LaunchYear(val year: Int, override val order: Order) : FilterData() {
        override fun reverse() = LaunchYear(this.year, order.revert())
    }

    @Parcelize
    class Success(val success: Boolean, override val order: Order) : FilterData() {
        override fun reverse() = Success(this.success, order.revert())
    }

    abstract val order:Order
    abstract fun reverse(): FilterData
}

enum class Order {
    ASCENDING, DESCENDING;

    fun revert() = when(this) {
        ASCENDING -> DESCENDING
        DESCENDING -> ASCENDING
    }
}

fun FilterData.describtion(res: Resources) =
    when(this) {
        is FilterData.All -> "${res.getString(R.string.filter_all)} ${this.order.getDescription(res)}"
        is FilterData.LaunchYear -> "${res.getString(R.string.filter_year)} $year ${this.order.getDescription(res)}"
        is FilterData.Success -> {
            "${
                when(success) {
                    true -> res.getString(R.string.filter_only_success)
                    false -> res.getString(R.string.filter_only_not_success)
                }
            } ${this.order.getDescription(res)}"
        }
    }

fun Order.getDescription(res: Resources) =
    when(this) {
        Order.ASCENDING -> res.getString(R.string.ascending)
        Order.DESCENDING -> res.getString(R.string.descending)
    }

fun FilterData.process(list: List<LaunchData>): List<LaunchData> {
    return when (this) {
        is FilterData.All -> list
        is FilterData.LaunchYear -> {
            list.filter {
                TimeFormatter.getYear(it.date) == this.year
            }
        }
        is FilterData.Success -> list.filter { it.launchStatus is LaunchStatus.Success == this.success }
    }.let {
        if (this.order == Order.DESCENDING) it.reversed()
        else it
    }
}