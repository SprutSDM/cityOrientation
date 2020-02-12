package ru.spbgororient.cityorientation.fragments.myTeam

import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.network.Network

class MyTeamPresenter(private val view: MyTeamContract.View,
                      private val dataController: DataController) : MyTeamContract.Presenter {

    override fun onViewCreated() {
        if (dataController.quests.questId == "") {
            view.disableLeaveQuestButton()
        } else {
            view.activateLeaveQuestButton()
        }
        view.setLogin(dataController.team.login)
        view.setTeamName(dataController.team.teamName)
        view.setPassword(dataController.team.password)
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
            dataController.renameTeam(teamName, ::callbackRenameTeam)
            view.hideKeyboard()
        }
    }

    override fun logout() {
        dataController.resetTeam()
        view.openLoginActivity()
    }

    override fun leaveQuest() {
        if (dataController.quests.questId != "") {
            dataController.leaveQuest(::callbackLeaveQuest)
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
