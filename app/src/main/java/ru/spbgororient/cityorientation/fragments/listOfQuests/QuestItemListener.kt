package ru.spbgororient.cityorientation.fragments.listOfQuests

import ru.spbgororient.cityorientation.quests.Quest

interface QuestItemListener {
    fun onJoinToQuestClick(clickedQuest: Quest)
}
