package com.pkurkowski.spacextest.presentation.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.pkurkowski.spacextest.R
import com.pkurkowski.spacextest.presentation.MainActivity
import com.pkurkowski.spacextest.presentation.TimeFormatter
import java.util.*

class FilterSelectYearDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "FilterSelectYearDialogFragment"
        private const val YEAR_RANGE = 100
        fun getInstance() = FilterSelectYearDialogFragment()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (activity as MainActivity).let { mainActivity ->
            val currentYear = TimeFormatter.getYear(Date())
            val builder = AlertDialog.Builder(mainActivity)
            val view = mainActivity.layoutInflater.inflate(R.layout.dialog_number_picker, null)
            val picker = view.findViewById<NumberPicker>(R.id.numberPicker)
            builder.setView(view)
            builder.setTitle(R.string.filter_select_year)

            picker.minValue = currentYear - YEAR_RANGE
            picker.maxValue = currentYear + YEAR_RANGE
            picker.value = currentYear

            builder.setPositiveButton(R.string.ok) { _: DialogInterface, _: Int ->
                mainActivity.filterUpdate(FilterUpdate.OnlyYear(picker.value))
            }
            builder.setNegativeButton(R.string.cancel) { _: DialogInterface, _: Int -> }
            builder.create()
        }
    }


}