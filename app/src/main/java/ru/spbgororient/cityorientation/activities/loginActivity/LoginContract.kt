package ru.spbgororient.cityorientation.activities.loginActivity

interface LoginContract {

    interface View {
        fun showLoadingData()
        fun hideLoadingData()
        fun hideKeyboard()
        fun showIncorrectLoginOrPassword()
        fun showFailOpenLink()
        fun showNoInternetConnection()
        fun openNavigationActivity()
        fun openVk(): Boolean
    }

    interface Presenter {

        fun tryLogin(login: String, password: String)
        fun onInputPasswordImeAction(login: String, password: String)
        fun openVk()
    }
}
