package ru.spbgororient.cityorientation

import android.app.Application
import android.content.Context
import ru.spbgororient.cityorientation.dataController.DataController
import ru.spbgororient.cityorientation.network.Network
import kotlin.concurrent.timer

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        DataController.initInstance(getSharedPreferences("settings", Context.MODE_PRIVATE))
        val getStateTimer = timer(name = "getStateTimer", initialDelay = 5000, period = 5000) {
            // Если мы залогинены и уже находимся на экране с NavigationActivity
            if (DataController.instance.team.isLogin) {
                DataController.instance.getStateForTimer(::updateData)
            }

        }
    }

    private fun updateData(response: Network.NetworkResponse, questId: String, step: Int, times: List<Int>, timesComplete: List<Int>) {
        if (response != Network.NetworkResponse.OK)
            return

        // Кто-то нажал кнопку покинуть квест
        if (questId == "" && DataController.instance.quests.questId != "") {
            // Кто-то нажал кнопку покинуть квест
            DataController.instance.quests.resetQuest()
            // TODO: Добавить переход с экрана квеста в список квестов, если данный экран отображается.
            return
        }

        // Кто-то выбрал квест
        // Возможно, кто-то нажал кнопку покинуть квест и выбрал новый.
        if (questId != "" && (DataController.instance.quests.questId == "" || DataController.instance.quests.questId != questId)) {
            DataController.instance.quests.questId = questId
            DataController.instance.quests.step = step
            DataController.instance.quests.times = times
            DataController.instance.quests.timesComplete = timesComplete
            DataController.instance.loadTasks {
                if (it == Network.NetworkResponse.OK) {
                    // TODO: Добавить переход на экран с ожиданием квеста / заданием.
                }
            }
            return
        }

        // Кто-то ввёл правильный ответ
        if (step != DataController.instance.quests.step) {
            DataController.instance.quests.step = step
            // TODO: Добавить переход к следующему квесту.
            return
        }
    }
}