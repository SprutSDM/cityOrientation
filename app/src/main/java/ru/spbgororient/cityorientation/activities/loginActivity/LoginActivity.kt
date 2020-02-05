package ru.spbgororient.cityorientation.activities.loginActivity

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_login.*
import ru.spbgororient.cityorientation.R
import ru.spbgororient.cityorientation.activities.mainActivity.MainActivity
import ru.spbgororient.cityorientation.dataController.DataController

class LoginActivity: AppCompatActivity(), LoginContract.View {
    private val presenter: LoginContract.Presenter by lazy { LoginPresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        DataController.instance.loadQuests({ })

        button_login.setOnClickListener {
            presenter.tryLogin(getLogin(), getPassword())
        }
        input_password.editText!!.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                presenter.onInputPasswordImeAction(getLogin(), getPassword())
                true
            } else {
                false
            }
        }
        image_vk_logo.setOnClickListener {
            presenter.openVk()
        }
        text_link_vk.setOnClickListener {
            presenter.openVk()
        }
    }

    override fun showLoadingData() {
        progress_bar.visibility = ProgressBar.VISIBLE
        button_login.isEnabled = false
    }

    override fun hideLoadingData() {
        progress_bar.visibility = ProgressBar.INVISIBLE
        button_login.isEnabled = true
    }

    override fun hideKeyboard() {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    override fun showIncorrectLoginOrPassword() = showSnackbar(R.string.snackbar_incorrect_login_or_password)

    override fun showFailOpenLink() = showSnackbar(R.string.snackbar_fail_when_open_link)

    override fun showNoInternetConnection() = showSnackbar(R.string.snackbar_no_internet_connection)

    private fun showSnackbar(@StringRes messageId: Int) {
        Snackbar.make(findViewById(R.id.activity_login), getString(messageId), Snackbar.LENGTH_SHORT).show()
    }

    override fun openNavigationActivity() {
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }

    override fun openVk(): Boolean {
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
