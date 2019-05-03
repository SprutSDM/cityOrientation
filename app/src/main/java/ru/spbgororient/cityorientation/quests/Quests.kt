package ru.spbgororient.cityorientation.quests

class Quests {
    var mapOfQuests: Map<String, Quest> = HashMap()
    var listOfTasks: List<Task> = ArrayList()
    var questId = ""
    var step = 0
    var times: List<Int> = ArrayList(0)
    var timesComplete: List<Int> = ArrayList(0)
    var isStarted = false

    /**
     * Возвращает текущее задание.
     */
    fun getTask(): Task {
        return listOfTasks[step]
    }

    fun setMapOfQuests(listOfQuests: List<Quest>) {
        mapOfQuests = listOfQuests.associate { Pair(it.questId, it) }
    }

    fun getListOfQuests(): ArrayList<Quest> {
        return ArrayList(mapOfQuests.values)
    }

    /**
     * Сбрасывает все сохраненные данные об текущем квесте.
     */
    fun resetQuest() {
        questId = ""
        step = 0
        listOfTasks = ArrayList()
        times = ArrayList()
        isStarted = false
    }
}