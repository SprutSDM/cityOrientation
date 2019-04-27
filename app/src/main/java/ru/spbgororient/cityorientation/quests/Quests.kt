package ru.spbgororient.cityorientation.quests

class Quests {
    var listOfQuests: List<Quest> = ArrayList()
    var listOfTasks: List<Task> = ArrayList()
    var questId: String = ""
    var step: Int = 0
    var times: List<Int> = ArrayList(0)
    var timesComplete: List<Int> = ArrayList(0)
    var isStarted = false

    /**
     * Возвращает текущее задание.
     */
    fun getTask(): Task {
        return listOfTasks[step]
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