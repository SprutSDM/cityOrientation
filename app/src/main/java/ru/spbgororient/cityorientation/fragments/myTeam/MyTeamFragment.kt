package ru.spbgororient.cityorientation.fragments.myTeam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_my_team.*
import ru.spbgororient.cityorientation.activities.LoginActivity
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.fragments.listOfQuests.ListOfQuestsFragment
import ru.spbgororient.cityorientation.network.Network

class MyTeamFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MyTeamFragment", "${DataController.instance.team.teamName}")
        Log.d("MyTeamFragment", "hashCode(): ${this.hashCode()}")
        // TODO: При перелогине в другой аккаунт остаются старые данные.
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
                hideKeyboard()
            }
        }
        edit_name_team.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    if (edit_name_team.text.toString() != "")
                        hideKeyboard()
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
            activity?.let {
                Snackbar.make(it.findViewById(R.id.content_frame), getString(R.string.team_renamed),
                    Snackbar.LENGTH_LONG).show()
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
                ListOfQuestsFragment.instance.notifyDataSetChanged()
                activity?.let {
                    Snackbar.make(it.findViewById(R.id.content_frame), getString(R.string.left_quest),
                        Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun hideKeyboard() {
        activity?.currentFocus?.let { v ->
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    companion object {
        var instance = MyTeamFragment()
    }
}