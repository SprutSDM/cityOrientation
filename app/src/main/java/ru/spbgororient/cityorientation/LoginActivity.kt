package ru.spbgororient.cityorientation

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_login.*
import ru.spbgororient.cityorientation.questsController.DataController
import android.view.inputmethod.InputMethodManager


class LoginActivity : AppCompatActivity() {

    fun callbackListOfTasks(ans: Boolean) {
        if (ans) {
            runOnUiThread { progressBar.visibility = ProgressBar.INVISIBLE }
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
        }
    }

    fun callbackGetState(ans: Boolean) {
        if (ans) {
            if (DataController.instance.questId == "Quest ID") {
                runOnUiThread { progressBar.visibility = ProgressBar.INVISIBLE }
                val intent = Intent(this, NavigationActivity::class.java)
                startActivity(intent)
            } else {
                DataController.instance.listOfTasks(::callbackListOfTasks)
            }
        } else {
            runOnUiThread { progressBar.visibility = ProgressBar.INVISIBLE }
        }
    }

    fun callbackLogin(ans: Boolean) {
        if (ans)
            DataController.instance.getState(::callbackGetState)
        else {
            runOnUiThread { progressBar.visibility = ProgressBar.INVISIBLE }
            Snackbar.make(findViewById(R.id.activityLogin), "Неправильный логин или пароль!", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        DataController.instance.loadQuests({})

        butContinue.setOnClickListener {
            val login = inputLogin.editText!!.text.toString()
            val password = inputPassword.editText!!.text.toString()
            if (login.isNotEmpty() && password.isNotEmpty()) {
                progressBar.visibility = ProgressBar.VISIBLE
                DataController.instance.loginTeam(login, password, ::callbackLogin)
            }
        }
        inputPassword.editText!!.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    val login = inputLogin.editText!!.text.toString()
                    val password = inputPassword.editText!!.text.toString()
                    if (login.isNotEmpty() && password.isNotEmpty()) {
                        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                            currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                        progressBar.visibility = ProgressBar.VISIBLE
                        DataController.instance.loginTeam(login, password, ::callbackLogin)
                    }
                    true
                }
                else -> false
            }
        }
    }

}
