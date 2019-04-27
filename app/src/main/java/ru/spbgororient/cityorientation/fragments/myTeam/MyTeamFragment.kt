package ru.spbgororient.cityorientation.fragments.myTeam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_my_team.*
import ru.spbgororient.cityorientation.activities.LoginActivity
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.network.Network

class MyTeamFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edit_name_team.setText(DataController.instance.team.teamName)
        edit_login_team.setText(DataController.instance.team.login)
        edit_password_team.setText(DataController.instance.team.password)
        if (DataController.instance.quests.questId == "")
            button_leave_quest.visibility = View.INVISIBLE

        check_box.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked)
                edit_password_team.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else
                edit_password_team.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        button_rename_team.setOnClickListener {
            if (edit_name_team.text.toString() != "") {
                DataController.instance.renameTeam(edit_name_team.text.toString(), ::callbackRenameTeam)
                (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    activity!!.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
        edit_name_team.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    if (edit_name_team.text.toString() != "")
                        (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                            activity!!.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                    DataController.instance.renameTeam(edit_name_team.text.toString(), ::callbackRenameTeam)
                    true
                }
                else -> false
            }
        }
        button_leave_team.setOnClickListener {
            DataController.instance.resetTeam()
            val intent = Intent(context, LoginActivity::class.java)
            activity!!.startActivity(intent)
        }
        button_leave_quest.setOnClickListener {
            if (DataController.instance.quests.questId != "")
                DataController.instance.leaveQuest(::callbackLeaveQuest)
        }
    }


    fun setVisibleLeaveButton() {
        activity?.runOnUiThread {
            activity?.findViewById<Button>(R.id.button_leave_quest)!!.visibility = View.VISIBLE
        }
    }

    /**
     * Вызывается при завершении запроса на переименование команды.
     */
    private fun callbackRenameTeam(response: Network.NetworkResponse) {
        if (response == Network.NetworkResponse.OK) {
            Snackbar.make(activity!!.findViewById(R.id.content_frame), "Команда успешно переименована!",
                Snackbar.LENGTH_LONG).show()
            activity?.runOnUiThread {
                activity?.findViewById<NavigationView>(R.id.navigation_view)?.getHeaderView(0)
                    ?.findViewById<TextView>(R.id.text_name_team)?.text = DataController.instance.team.teamName
            }
        }
    }

    /**
     * Вызывается при завершении запроса покидание квеста.
     */
    private fun callbackLeaveQuest(response: Network.NetworkResponse) {
        if (response == Network.NetworkResponse.OK) {
            activity?.runOnUiThread {
                button_leave_quest.visibility = View.INVISIBLE
                Snackbar.make(activity!!.findViewById(R.id.content_frame), "Вы успешно покинули текущий квест!",
                    Snackbar.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        var instance = MyTeamFragment()
    }
}