package com.pkurkowski.spacextest.presentation.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.pkurkowski.spacextest.R
import com.pkurkowski.spacextest.presentation.MainActivity

class FilterLaunchesDialogFragment: DialogFragment() {

    companion object {
        const val TAG = "FilterLaunchesDialogFragment"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (activity as MainActivity).let { mainActivity ->
            val map = mutableMapOf<String, FilterUpdate>()
                .apply {
                    put("reverse", FilterUpdate.ReverseOrder)
                    put("all", FilterUpdate.All)
                    put("only successful", FilterUpdate.OnlySuccess)
                    put("only not successful", FilterUpdate.OnlyNotSuccess)
                }.toMap()

            val keys = map.keys.toTypedArray()

            val builder = AlertDialog.Builder(mainActivity)
            builder.setTitle(R.string.menu_filter)
                .setItems(keys,
                    DialogInterface.OnClickListener { _, index ->
                        map[keys[index]]?.also { mainActivity.filterUpdate(it) }
                    })
            builder.create()
        }
    }


}