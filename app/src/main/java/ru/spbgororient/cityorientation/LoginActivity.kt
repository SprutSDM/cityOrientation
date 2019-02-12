package ru.spbgororient.cityorientation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_login.*
import ru.spbgororient.cityorientation.questsController.DataController

class LoginActivity : AppCompatActivity() {

    fun callback(ans: Boolean) {
        if (ans) {
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
        } else {

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
                DataController.instance.loginTeam(login, password, ::callback)
        }
    }
}
