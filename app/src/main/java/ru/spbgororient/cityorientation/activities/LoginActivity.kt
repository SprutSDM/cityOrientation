package ru.spbgororient.cityorientation.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
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
                progress_bar.visibility = ProgressBar.VISIBLE
                button_login.isEnabled = false
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
                        progress_bar.visibility = ProgressBar.VISIBLE
                        button_login.isEnabled = false
                        DataController.instance.signUp(login, password, ::callbackLogin)
                    }
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (DataController.instance.loadTeam()) {
            input_answer.editText?.setText(DataController.instance.team.login)
            input_password.editText?.setText(DataController.instance.team.password)
            progress_bar.visibility = ProgressBar.VISIBLE
            button_login.isEnabled = false
            DataController.instance.signUp(
                DataController.instance.team.login,
                DataController.instance.team.password, ::callbackLogin)
        }
    }

    /**
     * Вызывается при завершении загрузки списка задач.
     */
    private fun callbackListOfTasks(response: Network.NetworkResponse) {
        if (response == Network.NetworkResponse.OK) {
            runOnUiThread {
                progress_bar.visibility = ProgressBar.INVISIBLE
                button_login.isEnabled = true
            }
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
                runOnUiThread {
                    progress_bar.visibility = ProgressBar.INVISIBLE
                    button_login.isEnabled = true
                }
                val intent = Intent(this, NavigationActivity::class.java)
                startActivity(intent)
            } else { // Иначе грузим текущие задачи.
                DataController.instance.loadTasks(::callbackListOfTasks)
            }
        } else {
            runOnUiThread {
                progress_bar.visibility = ProgressBar.INVISIBLE
                button_login.isEnabled = true
            }
        }
    }

    /**
     * Вызывается при завершении login команды.
     */
    private fun callbackLogin(response: Network.NetworkResponse) {
        Log.d(LOG_KEY, response.toString())
        if (response == Network.NetworkResponse.OK)
            DataController.instance.getState(::callbackGetState)
        else {
            runOnUiThread {
                progress_bar.visibility = ProgressBar.INVISIBLE
                button_login.isEnabled = true
            }
            Snackbar.make(findViewById(R.id.activity_login), "Неправильный логин или пароль!", Snackbar.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val LOG_KEY = "LoginActivity"
    }
}
