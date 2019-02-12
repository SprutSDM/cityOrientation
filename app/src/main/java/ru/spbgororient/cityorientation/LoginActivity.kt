package ru.spbgororient.cityorientation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import ru.spbgororient.cityorientation.questsController.DataController

class LoginActivity : AppCompatActivity() {

    fun callbackListOfTasks(ans: Boolean) {
        if (ans) {
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
        }
    }

    fun callbackGetState(ans: Boolean) {
        if (ans) {
            if (DataController.instance.questId == "Quest ID") {
                val intent = Intent(this, NavigationActivity::class.java)
                startActivity(intent)
            } else
                DataController.instance.listOfTasks(::callbackListOfTasks)
        }
    }

    fun callbackLogin(ans: Boolean) {
        if (ans)
            DataController.instance.getState(::callbackGetState)
        else {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        DataController.instance.loadQuests({})

        butContinue.setOnClickListener {
            val login = inputLogin.editText!!.text.toString()
            val password = inputPassword.editText!!.text.toString()
            if (login.isNotEmpty() && password.isNotEmpty())
                DataController.instance.loginTeam(login, password, ::callbackLogin)
        }
    }
}
