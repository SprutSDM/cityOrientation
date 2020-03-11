package ru.spbgororient.cityorientation.fragments.listOfQuests

import ru.spbgororient.cityorientation.quests.Quest

interface ListOfQuestsContract {
    interface View {
        fun showQuests(quests: List<Quest>)
        fun showNoQuests()
    }

    interface Presenter {
        fun onViewCreated()
        fun joinToQuest(quest: Quest)
    }
}
