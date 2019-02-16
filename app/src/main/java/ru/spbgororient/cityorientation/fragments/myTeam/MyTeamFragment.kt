package ru.spbgororient.cityorientation.fragments.myTeam

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import ru.spbgororient.cityorientation.LoginActivity
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.questsController.DataController

class MyTeamFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editNameTeam.setText(DataController.instance.teamName)
        editLoginTeam.setText(DataController.instance.login)
        editPasswordTeam.setText(DataController.instance.password)
        if (DataController.instance.questId == "Quest ID")
            butLeaveQuest.visibility = View.INVISIBLE

        checkBox.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked)
                editPasswordTeam.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else
                editPasswordTeam.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        butRenameTeam.setOnClickListener {
            if (editNameTeam.text.toString() != "") {
                DataController.instance.renameTeam(editNameTeam.text.toString(), ::callbackRenameTeam)
                (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    activity!!.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
        editNameTeam.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    if (editNameTeam.text.toString() != "")
                        (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                            activity!!.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                        DataController.instance.renameTeam(editNameTeam.text.toString(), ::callbackRenameTeam)
                    true
                }
                else -> false
            }
        }
        butLeaveTeam.setOnClickListener {
            val editor = DataController.instance.mSettings.edit()
            editor.remove("login")
            editor.remove("password")
            editor.apply()
            val intent = Intent(context, LoginActivity::class.java)
            activity!!.startActivity(intent)
        }
        butLeaveQuest.setOnClickListener {
            if (DataController.instance.questId != "Quest ID")
                DataController.instance.leaveQuest(::callbackLeaveQuest)
        }
    }

    fun setVisibleLeaveButton() {
        activity?.runOnUiThread {
            activity?.findViewById<Button>(R.id.butLeaveQuest)!!.visibility = View.VISIBLE
        }
    }

    fun callbackRenameTeam(ans: Boolean) {
        if (ans) {
            Snackbar.make(activity!!.findViewById(R.id.content_frame), "Команда успешно переименована!",
                Snackbar.LENGTH_LONG).show()
            activity?.runOnUiThread {
                activity?.findViewById<NavigationView>(R.id.nav_view)?.getHeaderView(0)
                    ?.findViewById<TextView>(R.id.labelTeamName)?.text = DataController.instance.teamName
            }
        }
    }

    fun callbackLeaveQuest(ans: Boolean) {
        if (ans) {
            activity?.runOnUiThread {
                butLeaveQuest.visibility = View.INVISIBLE
                Snackbar.make(activity!!.findViewById(R.id.content_frame), "Вы успешно покинули текущий квест!",
                    Snackbar.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        val instance: MyTeamFragment by lazy { MyTeamFragment() }
    }
}