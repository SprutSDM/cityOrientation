package ru.spbgororient.cityorientation.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import ru.spbgororient.cityorientation.R

class MyTeamFragment: Fragment() {

    companion object {
        fun newInstance(): MyTeamFragment {
            return MyTeamFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val checkBox = getView()?.findViewById<CheckBox>(R.id.checkBox)
        val passwordEdit = getView()?.findViewById<EditText>(R.id.editPasswordTeam)

        checkBox?.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked)
                passwordEdit?.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else
                passwordEdit?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
    }
}