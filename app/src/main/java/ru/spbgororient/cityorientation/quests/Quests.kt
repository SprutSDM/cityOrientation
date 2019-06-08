package ru.spbgororient.cityorientation.quests

class Quests {
    var mapOfQuests: Map<String, Quest> = HashMap()
    var listOfTasks: List<Task> = ArrayList()
    var questId = "" // ID текущего квест
    var step = 0 // текущий этап
    var timesComplete: MutableList<Int> = ArrayList()
    var tips: MutableList<MutableList<Boolean>> = ArrayList()

    val isFinished : Boolean
        get() = step >= listOfTasks.size

    fun getQuest(): Quest? {
        return mapOfQuests[questId]
    }

    /**
     * Возвращает текущее задание.
     */
    fun getTask(): Task {
        return listOfTasks[step]
    }

    /**
     * Возвращает true, если на текущем задании подсказка с номером [number] была использована.
     */
    fun isUsedTip(number: Int): Boolean {
        return tips[step][number]
    }

    /**
     * Возвращает время, за которое был пройден предыдущее задание.
     */
    fun getTimeCompleteLastTask(): Int {
        if (step == 0)
            return 0
        return timesComplete[step - 1]
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
        tips = ArrayList()
    }
}