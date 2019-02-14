package ru.spbgororient.cityorientation.fragments.myTeam

import android.content.Context
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
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_my_team.*
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

        checkBox.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked)
                editPasswordTeam.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else
                editPasswordTeam.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        butRenameTeam.setOnClickListener {
            if (editNameTeam.text.toString() != "")
                DataController.instance.renameTeam(editNameTeam.text.toString(), ::callback)
        }
        editNameTeam.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    if (editNameTeam.text.toString() != "")
                        (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                            activity!!.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                        DataController.instance.renameTeam(editNameTeam.text.toString(), ::callback)
                    true
                }
                else -> false
            }
        }
    }

    fun callback(ans: Boolean) {
        if (ans) {
            Snackbar.make(activity!!.findViewById(R.id.activityLogin), "Команда успешно переименована!",
                Snackbar.LENGTH_LONG).show()
            activity?.findViewById<NavigationView>(R.id.nav_view)?.getHeaderView(0)
                ?.findViewById<TextView>(R.id.labelTeamName)?.text = DataController.instance.teamName
        }
    }

    companion object {
        val instance: MyTeamFragment by lazy { MyTeamFragment() }
    }
}