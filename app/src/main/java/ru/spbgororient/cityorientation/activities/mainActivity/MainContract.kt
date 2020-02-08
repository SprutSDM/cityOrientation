package ru.spbgororient.cityorientation.activities.mainActivity

interface MainContract {

    interface View {
        fun showMyTeam()
        fun showListOfQuests()

        fun showWaitingQuest()
        fun showTask()
        fun showFinishQuest()
        fun showNoQuestSelected()
    }

    interface Presenter {
        fun navigateToMyTeam()
        fun navigateToListOfQuests()
        fun navigateToQuest()
        fun updateTaskFragment()
    }
}
