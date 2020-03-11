package ru.spbgororient.cityorientation.activities.loginActivity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.activities.mainActivity.MainActivity
import ru.spbgororient.cityorientation.databinding.ActivityLoginBinding

class LoginActivity: AppCompatActivity(), OpenVkCallback, LoginClickCallback {
    val model: LoginViewModel by viewModels()

    private lateinit var loginEditView: EditText
    private lateinit var passwordEditView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        binding.callback = this
        binding.model = model
        //setContentView(R.layout.activity_login)

        loginEditView = findViewById(R.id.edit_login)
        passwordEditView = findViewById(R.id.edit_password)

        model.snackbarMessage.observe(this, Observer { message ->
            when (message) {
                LoginViewModel.LoginSnackbarMessage.INVALID_LOGIN_OR_PASSWORD ->
                    showSnackbar(R.string.snackbar_incorrect_login_or_password)
                LoginViewModel.LoginSnackbarMessage.NO_INTERNET_CONNECTION ->
                    showSnackbar(R.string.snackbar_no_internet_connection)
                LoginViewModel.LoginSnackbarMessage.UNABLE_OPEN_LING ->
                    showSnackbar(R.string.snackbar_fail_when_open_link)
                LoginViewModel.LoginSnackbarMessage.LOGIN_OR_PASSWORD_IS_EMPTY ->
                    showSnackbar(R.string.snackbar_login_or_password_is_empty)
                else -> Unit
            }
        })
        model.loginState.observe(this, Observer { state ->
            when (state) {
                LoginViewModel.LoginState.SUCCESS -> openNavigationActivity()
                else -> Unit
            }
        })
    }

    private fun showSnackbar(@StringRes messageId: Int) {
        Snackbar.make(findViewById(R.id.activity_login), getString(messageId), Snackbar.LENGTH_SHORT).show()
    }

    private fun openNavigationActivity() {
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }

    override fun onLoginClick() {
        model.tryLogin(getLogin(), getPassword())
    }

    override fun onVkOpenClick() {
        model.openVk(this)
    }

    override fun openVk(): Boolean {
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            return false
        }
        val url = "https://" + this.getString(R.string.website_url)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        return try {
            this.startActivity(intent)
            true
        } catch (e: ActivityNotFoundException) {
            false
        }
    }

    private fun getLogin(): String = loginEditView.text.toString()

    private fun getPassword(): String = passwordEditView.text.toString()
}
