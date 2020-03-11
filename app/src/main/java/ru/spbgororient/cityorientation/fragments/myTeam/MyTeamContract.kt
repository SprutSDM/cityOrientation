package ru.spbgororient.cityorientation.fragments.myTeam

interface MyTeamContract {
    interface View {
        fun setTeamName(teamName: String)
        fun setLogin(login: String)
        fun setPassword(password: String)
        fun activateLeaveQuestButton()
        fun disableLeaveQuestButton()
        fun showPassword()
        fun hidePassword()
        fun hideKeyboard()
        fun showTeamWasRenamed()
        fun showLeftQuest()
        fun openLoginActivity()
    }

    interface Presenter {
        fun onViewCreated()
        fun passwordCheckBoxChanged(isChecked: Boolean)
        fun renameTeam(teamName: String)
        fun logout()
        fun leaveQuest()
    }
}
