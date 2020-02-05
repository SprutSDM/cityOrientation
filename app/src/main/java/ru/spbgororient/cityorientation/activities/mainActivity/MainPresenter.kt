package ru.spbgororient.cityorientation.activities.mainActivity

import ru.spbgororient.cityorientation.dataController.DataController

class MainPresenter(private val view: MainContract.View) : MainContract.Presenter {
    override fun navigateToMyTeam() = view.showMyTeam()

    override fun navigateToListOfQuests() = view.showListOfQuests()

    override fun navigateToQuest() {
        val quest = DataController.instance.quests.getQuest()
        DataController.instance.quests.let {
            when {
                quest == null -> view.showNoQuestSelected()
                quest.startTime * 1000 > DataController.instance.currentTime -> view.showWaitingQuest()
                it.isFinished ||(quest.startTime + quest.duration) * 1000
                        <= DataController.instance.currentTime -> view.showFinishQuest()
                it.getTask().img == "" -> view.showTask()
            }
        }
    }
}
