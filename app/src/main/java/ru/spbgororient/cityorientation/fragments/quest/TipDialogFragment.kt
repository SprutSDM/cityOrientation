package ru.spbgororient.cityorientation.fragments.quest

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*

class TipDialogFragment: DialogFragment() {
    val sdf = SimpleDateFormat("HHч. mmмин. ssсек.")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val builder = AlertDialog.Builder(activity)
        val time = arguments?.getInt("time")
        return builder
            .setTitle("Получить подсказку")
            .setMessage("Вы действительно хотите воспользоваться подсказкой №${targetRequestCode + 1}? Это добавит ${sdf.format(time)} штрафного времени!")
            .setPositiveButton("OK") { _, _ ->
                targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, null)
            }
            .setNegativeButton("Отмена", null)
            .create()
    }
}