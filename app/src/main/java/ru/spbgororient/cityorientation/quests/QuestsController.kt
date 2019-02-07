package ru.spbgororient.cityorientation.quests

class QuestsController private constructor() {
    var listOfQuests: MutableList<Quest> = ArrayList()

    fun loadQuests() {
        var quest1 = Quest(name = "Ориентирование", date = "24.02.12 в 15", place = "Выборгский район", amountOfCp = 14)
        listOfQuests.add(quest1)

        var quest2 = Quest(name = "Спортивное ориентирование", date = "02.08.19 в 15:47", place = "Петроградка район", amountOfCp = 112)
        listOfQuests.add(quest2)
    }

    companion object {
        val instance: QuestsController by lazy { QuestsController() }
    }
}