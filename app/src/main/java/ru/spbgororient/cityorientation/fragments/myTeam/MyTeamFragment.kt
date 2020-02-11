package ru.spbgororient.cityorientation.fragments.myTeam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_my_team.*
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.activities.loginActivity.LoginActivity
import ru.spbgororient.cityorientation.dataController.DataController

class MyTeamFragment: androidx.fragment.app.Fragment(), MyTeamContract.View {
    private val presenter: MyTeamContract.Presenter by lazy { MyTeamPresenter(this) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: При перелогине в другой аккаунт остаются старые данные.
        if (DataController.instance.quests.questId == "")
            button_leave_quest.visibility = View.INVISIBLE

        check_box.setOnCheckedChangeListener { _, isChecked ->
            presenter.passwordCheckBoxChanged(isChecked)
        }
        button_rename_team.setOnClickListener {
            presenter.renameTeam(edit_team_name.text.toString())
        }

        edit_team_name.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                presenter.renameTeam(edit_team_name.text.toString())
                true
            } else {
                false
            }
        }
        button_leave_team.setOnClickListener {
            presenter.logout()
        }
        button_leave_quest.setOnClickListener {
            presenter.leaveQuest()
        }
        presenter.onViewCreated()
    }

    override fun setTeamName(teamName: String) = edit_team_name.setText(teamName)

    override fun setLogin(login: String) = edit_login_team.setText(login)

    override fun setPassword(password: String) = edit_password_team.setText(password)

    override fun showPassword() {
        edit_password_team.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    }

    override fun hidePassword() {
        edit_password_team.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    override fun hideKeyboard() {
        activity?.currentFocus?.let { v ->
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    override fun showTeamWasRenamed() = showSnackbar(R.string.team_renamed)

    override fun showLeftQuest() = showSnackbar(R.string.left_quest)

    override fun openLoginActivity() {
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity?.startActivity(intent)
    }

    override fun activateLeaveQuestButton() {
        button_leave_quest.visibility = View.VISIBLE
    }

    override fun disableLeaveQuestButton() {
        button_leave_quest.visibility = View.GONE
    }

    private fun showSnackbar(@StringRes messageId: Int) {
        activity?.let { a ->
            Snackbar.make(a.findViewById(R.id.content_frame), getString(messageId), Snackbar.LENGTH_SHORT).show()
        }
    }

    fun setVisibleLeaveButton() {
        activity?.runOnUiThread {
            activity?.findViewById<Button>(R.id.button_leave_quest)!!.visibility = View.VISIBLE
        }
    }

    companion object {
        var instance = MyTeamFragment()
    }
}
