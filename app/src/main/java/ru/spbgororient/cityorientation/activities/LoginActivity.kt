package ru.spbgororient.cityorientation.activities

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_login.*
import android.view.inputmethod.InputMethodManager
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.network.Network


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        DataController.instance.loadQuests{}

        button_login.setOnClickListener {
            val login = input_answer.editText!!.text.toString()
            val password = input_password.editText!!.text.toString()
            if (login.isNotEmpty() && password.isNotEmpty()) {
                showProgressBar()
                DataController.instance.signUp(login, password, ::callbackLogin)
            }
        }
        input_password.editText!!.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    val login = input_answer.editText!!.text.toString()
                    val password = input_password.editText!!.text.toString()
                    if (login.isNotEmpty() && password.isNotEmpty()) {
                        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                            currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                        showProgressBar()
                        DataController.instance.signUp(login, password, ::callbackLogin)
                    }
                    true
                }
                else -> false
            }
        }
        image_vk_logo.setOnClickListener {
            openVk()
        }
        text_link_vk.setOnClickListener {
            openVk()
        }
    }

    override fun onResume() {
        super.onResume()
        if (DataController.instance.loadTeam()) {
            input_answer.editText?.setText(DataController.instance.team.login)
            input_password.editText?.setText(DataController.instance.team.password)
            showProgressBar()
            DataController.instance.signUp(
                DataController.instance.team.login,
                DataController.instance.team.password, ::callbackLogin)
        }
    }

    private fun showProgressBar() {
        progress_bar.visibility = ProgressBar.VISIBLE
        button_login.isEnabled = false
    }

    private fun hideProgressBar() {
        runOnUiThread {
            progress_bar.visibility = ProgressBar.INVISIBLE
            button_login.isEnabled = true
        }
    }

    /**
     * Вызывается при завершении загрузки списка задач.
     */
    private fun callbackListOfTasks(response: Network.NetworkResponse) {
        if (response == Network.NetworkResponse.OK) {
            hideProgressBar()
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Вызывается при завершении загрузки текущего состояния.
     */
    private fun callbackGetState(response: Network.NetworkResponse) {
        if (response == Network.NetworkResponse.OK) {
            // Если текущий квест не выбран, то сразу переходим на NavigationActivity
            if (DataController.instance.quests.questId == "") {
                hideProgressBar()
                val intent = Intent(this, NavigationActivity::class.java)
                startActivity(intent)
            } else { // Иначе грузим текущие задачи.
                DataController.instance.loadTasks(::callbackListOfTasks)
            }
        } else {
            hideProgressBar()
        }
    }

    /**
     * Вызывается при завершении login команды.
     */
    private fun callbackLogin(response: Network.NetworkResponse) {
        if (response == Network.NetworkResponse.OK)
            DataController.instance.getState(::callbackGetState)
        else {
            hideProgressBar()
            Snackbar.make(findViewById(R.id.activity_login),
                when (response) {
                    Network.NetworkResponse.NETWORK_ERROR -> getString(R.string.no_internet_connection)
                    else -> getString(R.string.incorrect_login_or_password)
                },
                Snackbar.LENGTH_LONG).show()
        }
    }

    private fun openVk() {
        val url = "https://" + getString(R.string.website_url)
        var intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Snackbar.make(findViewById(R.id.activity_login),
                getString(R.string.fail_when_open_link),
                Snackbar.LENGTH_SHORT).show()
        }
    }
}
