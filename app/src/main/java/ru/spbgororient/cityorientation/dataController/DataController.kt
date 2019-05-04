package ru.spbgororient.cityorientation.dataController

import android.content.SharedPreferences
import android.util.Log
import ru.spbgororient.cityorientation.network.Network
import ru.spbgororient.cityorientation.quests.Quests
import ru.spbgororient.cityorientation.team.Team

/**
 * Этот класс производит контроль данных, получаемых через [Network].
 *
 * Сохраняет некоторые данные сразу в [Team] и [Quests], убирая таким образом эту обязанность с кода,
 * в котором вызываются эти методы. Данные возвращаются через callback.
 */
class DataController private constructor(private val sharedPreferences: SharedPreferences,
                                         val team: Team,
                                         val quests: Quests,
                                         private val network: Network) {
    var timeZone: Long = 0
    var timeOffset: Long = 0
    val currentTime: Long
        get() = System.currentTimeMillis() + timeOffset

    /**
     * Загружает login, password команды из внутреннего хранилища.
     *
     * @return возвращает true, если данные были успешно считаны. Иначе false.
     */
    fun loadTeam(): Boolean {
        return team.load(sharedPreferences)
    }

    /**
     * Производит login команды.
     *
     * Вызывает метод [Network.signUp], который возвращает два параметра: response, teamName.
     * Затем вызывается сам [callback].
     *
     * @param[login] логин команды.
     * @param[password] пароль команды.
     * @param[callback] вызывается при завершении запроса.
     */
    fun signUp(login: String, password: String, callback: (response: Network.NetworkResponse) -> Unit) {
        Log.d("DataController", "loginTeam login:$login, password:$password")
        network.signUp(login, password) { response, teamName ->
            if (response == Network.NetworkResponse.OK) {
                team.save(login, password, sharedPreferences)
                team.rename(teamName)
            }
            Log.d("DataController", response.toString())
            callback(response)
        }
    }

    /**
     * Сбрасывает данные об текущей команде.
     */
    fun resetTeam() {
        team.reset(sharedPreferences)
    }

    /**
     * Переименовывает название команды.
     *
     * Вызывает метод [Network.renameTeam], который возвращает response.
     * В случае успешного ok переименовывает команду. Затем вызывается [callback].
     *
     * @param[teamName] новое название команды.
     * @param[callback] вызывается при завершении запроса.
     */
    fun renameTeam(teamName: String, callback: (response: Network.NetworkResponse) -> Unit) {
        network.renameTeam(team.login, teamName) { response ->
            if (response == Network.NetworkResponse.OK)
                team.rename(teamName)
            callback(response)
        }
    }

    /**
     * Добавляет команду к участю в квесте c id [questId].
     *
     * @param[questId] id квеста.
     * @param[callback] вызывается при завершении запроса.
     */
    fun joinToQuest(questId: String, callback: (response: Network.NetworkResponse) -> Unit) {
        network.joinToQuest(team.login, questId) { response ->
            if (response == Network.NetworkResponse.OK)
                quests.questId = questId
            callback(response)
        }
    }

    /**
     * Убирает команду из участия в текущем квесте.
     *
     * @param[callback] вызывается при завершении запроса.
     */
    fun leaveQuest(callback: (response: Network.NetworkResponse) -> Unit) {
        network.leaveQuest(team.login, quests.questId) { response ->
            if (response == Network.NetworkResponse.OK)
                quests.resetQuest()
            callback(response)
        }
    }

    /**
     * Получает список всех квестов.
     *
     * @param[callback] вызывается при завершении запроса.
     */
    fun loadQuests(callback: (response: Network.NetworkResponse) -> Unit) {
        network.loadQuests { response, listOfQuests ->
            if (response == Network.NetworkResponse.OK)
                quests.setMapOfQuests(listOfQuests)
            callback(response)
        }
    }

    /**
     * Получает список всех заданий для текущего квеста.
     *
     * @param[callback] вызывается при завершении запроса.
     */
    fun loadTasks(callback: (response: Network.NetworkResponse) -> Unit) {
        network.loadTasks(team.login, quests.questId) { response, listOfTasks ->
            if (response == Network.NetworkResponse.OK) {
                quests.listOfTasks = listOfTasks
                /* Устанавливаем времена прохождения равные -1.
                   Необходимо сделать это только в том случае, если мы не загрузили этот массив в getState */
                if (quests.timesComplete.size == 0)
                    quests.timesComplete = MutableList(listOfTasks.size) {-1}
            }
            callback(response)
        }
    }

    /**
     * Получает текущее состояние.
     *
     * Обновляет данные, которые могли измениться другими участниками команды. Вызывается после логина команды для
     * инициализации текущих данных
     *
     * @param[callback] вызывается при завершении запроса.
     */
    fun getState(callback: (response: Network.NetworkResponse) -> Unit) {
        network.getState(team.login) { response, data ->
            if (response == Network.NetworkResponse.OK) {
                team.teamName = data.teamName
                timeZone = data.timeZone
                timeOffset = data.seconds - System.currentTimeMillis()
                with(quests) {
                    this.questId = data.questId
                    this.step = data.step
                    this.timesComplete = data.timesComplete
                }
            }
            callback(response)
        }
    }

    /**
     * Получает текущее состояние.
     *
     * Возвращает в callback данные, которые могли измениться другими участниками команды. Данные должны быть сохранены
     * в callback'е. Этот метод используется для обновления в фоне. Благодаря такому callback'у можно определить
     * какое действие совершили
     *
     * @param[callback] вызывается при завершении запроса.
     */
    /*fun getStateForTimer(callback: (response: Network.NetworkResponse, questId: String, step: Int, startTime: Long, times: List<Int>, timesComplete: List<Int>) -> Unit) {
        network.getState(team.login) { response, data ->
            if (response == Network.NetworkResponse.OK) {
                team.teamName = data.teamName
            }
            callback(response, questId, step, startTime, times, timeZone, timesComplete)
        }
    }*/

    /**
     * Уведомлеят о том, что текущее задание из текущего квеста решено верно.
     *
     * @param[callback] вызывается при завершении запроса.
     */
    fun completeTask(callback: (response: Network.NetworkResponse) -> Unit) {
        network.completeTask(team.login, quests.questId, quests.step) { response, timeComplete ->
            if (response == Network.NetworkResponse.OK) {
                quests.timesComplete[quests.step] = timeComplete
                quests.step += 1
            }
            callback(response)
        }
    }

    companion object {
        lateinit var instance: DataController

        fun initInstance(sharedPreferences: SharedPreferences){
            if (::instance.isInitialized)
                return
            instance = DataController(sharedPreferences, Team(), Quests(), Network.instance)
        }
    }
}
