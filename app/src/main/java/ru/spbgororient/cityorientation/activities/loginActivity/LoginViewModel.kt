package ru.spbgororient.cityorientation.activities.loginActivity

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.spbgororient.cityorientation.App
import ru.spbgororient.cityorientation.network.Network

class LoginViewModel(app: Application) : AndroidViewModel(app) {

    private val dataController = (app as App).dataController
    var loginState = MutableLiveData<LoginState>(LoginState.INPUT_DATA)
    val snackbarMessage = MutableLiveData<LoginSnackbarMessage>(LoginSnackbarMessage.NO)

    init {
        dataController.loadQuests {  }
    }

    fun tryLogin(login: String, password: String) {
        if (!loginAndPasswordNotEmpty(login, password)) {
            snackbarMessage.value = LoginSnackbarMessage.LOGIN_OR_PASSWORD_IS_EMPTY
            return
        }
        loginState.value = LoginState.IN_PROGRESS
        dataController.signUp(login, password, ::callbackLogin)
    }

    fun openVk(callback: OpenVkCallback) {
        if (!callback.openVk()) {
            snackbarMessage.value = LoginSnackbarMessage.UNABLE_OPEN_LING
        }
    }

    private fun loginAndPasswordNotEmpty(login: String, password: String) = login.isNotEmpty() && password.isNotEmpty()

    /**
     * Вызывается при завершении login команды.
     */
    private fun callbackLogin(response: Network.NetworkResponse) {
        when (response) {
            Network.NetworkResponse.OK -> return dataController.getState(::callbackGetState)
            Network.NetworkResponse.FAILURE -> snackbarMessage.value = LoginSnackbarMessage.INVALID_LOGIN_OR_PASSWORD
            Network.NetworkResponse.NETWORK_ERROR -> snackbarMessage.value = LoginSnackbarMessage.NO_INTERNET_CONNECTION
            else -> Unit
        }
        loginState.value = LoginState.INPUT_DATA
    }

    /**
     * Вызывается при завершении загрузки текущего состояния.
     */
    private fun callbackGetState(response: Network.NetworkResponse) {
        if (response == Network.NetworkResponse.OK) {
            // Если текущий квест не выбран, то сразу переходим на MainActivity
            if (dataController.quests.questId == "") {
                loginState.value = LoginState.SUCCESS
            } else { // Иначе грузим текущие задачи.
                dataController.loadTasks(::callbackListOfTasks)
            }
        } else {
            loginState.value = LoginState.INPUT_DATA
            snackbarMessage.value = LoginSnackbarMessage.NO_INTERNET_CONNECTION
        }
    }

    /**
     * Вызывается при завершении загрузки списка задач.
     */
    private fun callbackListOfTasks(response: Network.NetworkResponse) {
        if (response == Network.NetworkResponse.OK) {
            loginState.value = LoginState.SUCCESS
        }
    }

    enum class LoginState {
        IN_PROGRESS,
        SUCCESS,
        INPUT_DATA
    }

    enum class LoginSnackbarMessage {
        INVALID_LOGIN_OR_PASSWORD,
        NO_INTERNET_CONNECTION,
        UNABLE_OPEN_LING,
        LOGIN_OR_PASSWORD_IS_EMPTY,
        NO
    }
}
