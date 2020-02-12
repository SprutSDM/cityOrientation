package ru.spbgororient.cityorientation.fragments.listOfQuests

import ru.spbgororient.cityorientation.activities.mainActivity.MainContract
import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.network.Network
import ru.spbgororient.cityorientation.quests.Quest

class ListOfQuestsPresenter(private val view: ListOfQuestsContract.View,
                            private val mainPresenter: MainContract.Presenter,
                            private val dataController: DataController) : ListOfQuestsContract.Presenter {
    override fun joinToQuest(quest: Quest) {
        dataController.joinToQuest(quest.questId, ::callbackApply)
    }

    override fun onViewCreated() {
        if (dataController.quests.getListOfQuests().size == 0) {
            view.showNoQuests()
        } else {
            view.showQuests(dataController.quests.getListOfQuests())
        }
    }

    /**
     * Вызывается, когда приходит ответ на запрос участия в квесте.
     */
    private fun callbackApply(response: Network.NetworkResponse) {
        if (response == Network.NetworkResponse.OK) {
            // TODO refactor
            // MyTeamFragment.instance.setVisibleLeaveButton()
            // notifyDataSetChanged()
            dataController.loadTasks(::callbackListOfTasks)
        }
    }

    /**
     * Вызывается, когда приходит список заданий.
     * Переходит на экран с квестом.
     */
    private fun callbackListOfTasks(response: Network.NetworkResponse) {
        if (response == Network.NetworkResponse.OK) {
            // WaitingToStartFragment.newInstance() // TODO: разобраться, нужно ли это
            mainPresenter.navigateToQuest()
        }
    }
}
