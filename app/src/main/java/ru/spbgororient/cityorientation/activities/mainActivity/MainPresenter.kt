package ru.spbgororient.cityorientation.activities.mainActivity

import ru.spbgororient.cityorientation.dataController.DataController

class MainPresenter(private val view: MainContract.View,
                    private val dataController: DataController) : MainContract.Presenter {
    override fun navigateToMyTeam() = view.showMyTeam()

    override fun navigateToListOfQuests() = view.showListOfQuests()

    override fun navigateToQuest() = updateTaskFragment()

    override fun updateTaskFragment() {
        val quest = dataController.quests.getQuest()
        dataController.quests.let {
            when {
                quest == null -> view.showNoQuestSelected()
                quest.startTime * 1000 > dataController.currentTime -> view.showWaitingQuest()
                it.isFinished || (quest.startTime + quest.duration) * 1000
                        <= dataController.currentTime -> view.showFinishQuest()
                else -> view.showTask()
            }
        }
    }
}
