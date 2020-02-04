package ru.spbgororient.cityorientation.activities.loginActivity

import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.network.Network

class LoginPresenter(private val view: LoginContract.View): LoginContract.Presenter {

    override fun tryLogin(login: String, password: String) {
        if (!loginAndPasswordNotEmpty(login, password)) {
            return
        }
        view.showLoadingData()
        DataController.instance.signUp(login, password, ::callbackLogin)
    }

    override fun onInputPasswordImeAction(login: String, password: String) {
        if (!loginAndPasswordNotEmpty(login, password)) {
            return
        }
        view.hideKeyboard()
        view.showLoadingData()
        DataController.instance.signUp(login, password, ::callbackLogin)
    }

    override fun openVk() {
        if (!view.openVk()) {
            view.showFailOpenLink()
        }
    }

    private fun loginAndPasswordNotEmpty(login: String, password: String) = login.isNotEmpty() && password.isNotEmpty()

    /**
     * Вызывается при завершении login команды.
     */
    private fun callbackLogin(response: Network.NetworkResponse) {
        if (response == Network.NetworkResponse.OK)
            DataController.instance.getState(::callbackGetState)
        else {
            view.hideLoadingData()
            view.showNoInternetConnection()
        }
    }

    /**
     * Вызывается при завершении загрузки текущего состояния.
     */
    private fun callbackGetState(response: Network.NetworkResponse) {
        if (response == Network.NetworkResponse.OK) {
            // Если текущий квест не выбран, то сразу переходим на NavigationActivity
            if (DataController.instance.quests.questId == "") {
                view.hideLoadingData()
                view.openNavigationActivity()
            } else { // Иначе грузим текущие задачи.
                DataController.instance.loadTasks(::callbackListOfTasks)
            }
        } else {
            view.hideLoadingData()
        }
    }

    /**
     * Вызывается при завершении загрузки списка задач.
     */
    private fun callbackListOfTasks(response: Network.NetworkResponse) {
        if (response == Network.NetworkResponse.OK) {
            view.hideLoadingData()
            view.openNavigationActivity()
        }
    }
}
