package ru.spbgororient.cityorientation.dataController

import android.content.SharedPreferences
import android.util.Log
import ru.spbgororient.cityorientation.network.Network
import ru.spbgororient.cityorientation.quests.Quests
import ru.spbgororient.cityorientation.team.Team
import java.util.*

class DataController private constructor(private val sharedPreferences: SharedPreferences,
                                         val team: Team,
                                         val quests: Quests,
                                         private val network: Network) {
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
     * Добавляет команду к участю в квесте, который находится в массиве [Quests.listOfQuests] на позиции [pos].
     *
     * @param[pos] позиция квеста в массиве [Quests.listOfQuests].
     * @param[callback] вызывается при завершении запроса.
     */
    fun joinToQuest(pos: Int, callback: (response: Network.NetworkResponse) -> Unit) {
        val questId = quests.listOfQuests[pos].questId
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
                quests.listOfQuests = listOfQuests
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
            if (response == Network.NetworkResponse.OK)
                quests.listOfTasks = listOfTasks
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
        network.getState(team.login) { response, teamName, questId, step, times, timesComplete ->
            if (response == Network.NetworkResponse.OK) {
                team.teamName = teamName
                with(quests) {
                    this.questId = questId
                    this.step = step
                    this.times = times
                    this.timesComplete = timesComplete
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
    fun getStateForTimer(callback: (response: Network.NetworkResponse, questId: String, step: Int, times: List<Int>, timesComplete: List<Int>) -> Unit) {
        network.getState(team.login) { response, teamName, questId, step, times, timesComplete ->
            if (response == Network.NetworkResponse.OK) {
                team.teamName = teamName
            }
            callback(response, questId, step, times, timesComplete)
        }
    }

    /**
     * Уведомлеят о том, что текущее задание из текущего квеста решено верно.
     *
     * @param[callback] вызывается при завершении запроса.
     */
    fun completeTask(callback: (response: Network.NetworkResponse) -> Unit) {
        network.completeTask(team.login, quests.questId, quests.step) { response ->
            if (response == Network.NetworkResponse.OK) {
                quests.step += 1

            } // TODO: Доделать метод
            callback(response)
        }
    }

    companion object {
        lateinit var instance: DataController

        fun initInstance(sharedPreferences: SharedPreferences){
            if (::instance.isInitialized)
                return
            instance = DataController(sharedPreferences, Team(), Quests(), Network.newInstance()!!)
        }
    }
}
