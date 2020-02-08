package ru.spbgororient.cityorientation.fragments.task

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import ru.spbgororient.cityorientation.R
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class TipDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val sdf = SimpleDateFormat(getString(R.string.sdf_time_with_exlp), Locale.UK)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val builder = AlertDialog.Builder(activity)
        val time = arguments?.getInt("time")
        return builder
            .setTitle(getString(R.string.get_tip))
            .setMessage(getString(R.string.warning_get_tip, targetRequestCode + 1, sdf.format(time)))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, null)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .create()
    }
}
