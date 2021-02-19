package com.pkurkowski.spacextest.presentation.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.pkurkowski.spacextest.R
import com.pkurkowski.spacextest.presentation.FilterData
import com.pkurkowski.spacextest.presentation.MainActivity
import com.pkurkowski.spacextest.presentation.Order

class FilterLaunchesDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "FilterLaunchesDialogFragment"
        const val FILTER_DATA_KEY = "FilterDataKey"

        fun getInstance(currentFilter: FilterData) = FilterLaunchesDialogFragment().also {
            it.arguments = bundleOf(
                FILTER_DATA_KEY to currentFilter
            )
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (activity as MainActivity).let { mainActivity ->

            val currentFilter = requireArguments().getParcelable<FilterData>(FILTER_DATA_KEY)!!

            val map = mutableMapOf<String, FilterUpdate>()
                .apply {

                    when (currentFilter.order) {
                        Order.ASCENDING -> put(
                            getString(R.string.filter_order_descending),
                            FilterUpdate.ReverseOrder
                        )
                        Order.DESCENDING -> put(
                            getString(R.string.filter_order_ascending),
                            FilterUpdate.ReverseOrder
                        )
                    }

                    if (currentFilter !is FilterData.All) put(
                        getString(R.string.filter_all),
                        FilterUpdate.All
                    )

                    if (currentFilter is FilterData.Success) {
                        when (currentFilter.success) {
                            true -> put(
                                getString(R.string.filter_only_not_success),
                                FilterUpdate.OnlyNotSuccess
                            )
                            false -> put(
                                getString(R.string.filter_only_success),
                                FilterUpdate.OnlySuccess
                            )
                        }
                    } else {
                        put(getString(R.string.filter_only_success), FilterUpdate.OnlySuccess)
                        put(
                            getString(R.string.filter_only_not_success),
                            FilterUpdate.OnlyNotSuccess
                        )
                    }

                }.toMap()

            val selectYearKey = getString(R.string.filter_year_select)
            val keys = map.keys.plus(selectYearKey).toTypedArray()

            val builder = AlertDialog.Builder(mainActivity)
            builder.setTitle(R.string.menu_filter)
                .setItems(keys,
                    DialogInterface.OnClickListener { _, index ->

                        when (keys[index]) {
                            selectYearKey -> mainActivity.filterYearSelect()
                            else -> map[keys[index]]?.also { mainActivity.filterUpdate(it) }
                        }

                    })
            builder.create()
        }
    }


}