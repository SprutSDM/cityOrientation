package ru.spbgororient.cityorientation.activities.loginActivity

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_login.*
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.activities.mainActivity.MainActivity

class LoginActivity: AppCompatActivity(), OpenVkCallback, LoginClickCallback {
    val model: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        input_password.editText?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                model.tryLogin(getLogin(), getPassword())
                hideKeyboard()
                true
            } else {
                false
            }
        }
        model.snackbarMessage.observe(this, Observer { message ->
            when (message) {
                LoginViewModel.LoginSnackbarMessage.INVALID_LOGIN_OR_PASSWORD ->
                    showSnackbar(R.string.snackbar_incorrect_login_or_password)
                LoginViewModel.LoginSnackbarMessage.NO_INTERNET_CONNECTION ->
                    showSnackbar(R.string.snackbar_no_internet_connection)
                LoginViewModel.LoginSnackbarMessage.UNABLE_OPEN_LING ->
                    showSnackbar(R.string.snackbar_fail_when_open_link)
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

    private fun hideKeyboard() {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
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

    private fun getLogin(): String = input_login.editText!!.text.toString()

    private fun getPassword(): String = input_password.editText!!.text.toString()
}
