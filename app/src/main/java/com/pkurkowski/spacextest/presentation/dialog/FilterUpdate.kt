package com.pkurkowski.spacextest.presentation.dialog

sealed class FilterUpdate {
    object ReverseOrder: FilterUpdate()
    object All: FilterUpdate()
    object OnlySuccess: FilterUpdate()
    object OnlyNotSuccess: FilterUpdate()
//    object AllYears: FilterUpdate()
//    data class OnlyYear(val year: Int): FilterUpdate()
}
