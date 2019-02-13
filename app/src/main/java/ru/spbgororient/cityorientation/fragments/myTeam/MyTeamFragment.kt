package ru.spbgororient.cityorientation.fragments.myTeam

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            DataController.instance.renameTeam(editNameTeam.text.toString(), ::callback)
        }
    }

    fun callback(ans: Boolean) {
        if (ans)
            activity?.findViewById<NavigationView>(R.id.nav_view)?.getHeaderView(0)?.findViewById<TextView>(R.id.labelTeamName)?.text = DataController.instance.teamName
    }

    companion object {
        val instance: MyTeamFragment by lazy { MyTeamFragment() }
    }
}