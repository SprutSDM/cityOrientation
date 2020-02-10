package ru.spbgororient.cityorientation.fragments.myTeam

import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.network.Network

class MyTeamPresenter(private val view: MyTeamContract.View) : MyTeamContract.Presenter {

    override fun onViewCreated() {
        if (DataController.instance.quests.questId == "") {
            view.disableLeaveQuestButton()
        } else {
            view.activateLeaveQuestButton()
        }
        view.setLogin(DataController.instance.team.login)
        view.setTeamName(DataController.instance.team.teamName)
        view.setPassword(DataController.instance.team.password)
    }

    override fun passwordCheckBoxChanged(isChecked: Boolean) {
        if (isChecked) {
            view.showPassword()
        } else {
            view.hidePassword()
        }
    }

    override fun renameTeam(teamName: String) {
        if (teamName != "") {
            DataController.instance.renameTeam(teamName, ::callbackRenameTeam)
            view.hideKeyboard()
        }
    }

    override fun logout() {
        DataController.instance.resetTeam()
        view.openLoginActivity()
    }

    override fun leaveQuest() {
        if (DataController.instance.quests.questId != "") {
            DataController.instance.leaveQuest(::callbackLeaveQuest)
        }
    }

    /**
     * Вызывается при завершении запроса на переименование команды.
     */
    private fun callbackRenameTeam(response: Network.NetworkResponse) {
        if (response == Network.NetworkResponse.OK) {
            view.showTeamWasRenamed()
        }
    }

    /**
     * Вызывается при завершении запроса покидание квеста.
     */
    private fun callbackLeaveQuest(response: Network.NetworkResponse) {
        if (response == Network.NetworkResponse.OK) {
            view.disableLeaveQuestButton()
            // TODO refactor
            // ListOfQuestsFragment.instance.notifyDataSetChanged()
            view.showLeftQuest()
        }
    }
}
