package ru.spbgororient.cityorientation.network

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.*
import ru.spbgororient.cityorientation.quests.Quest
import ru.spbgororient.cityorientation.quests.Quests
import ru.spbgororient.cityorientation.quests.Task
import ru.spbgororient.cityorientation.team.Team
import java.io.IOException

/**
 * Этот класс содержит методы для общения с Rest Api сервером.
 *
 * Все запросы выполняются в фоновом потоке. У каждого метода есть callback, который вызывается
 * по завершению запроса. Первым аргументом всегда идёт Response.
 */
class Network private constructor() {
    enum class NetworkResponse {
        OK,
        LOADING,
        ERROR
    }

    private val gsonBuilder = GsonBuilder().create()
    private val client = OkHttpClient()

    /**
     * Создаёт запрос.
     *
     * @param[uri] относительный URI нужного REST запроса.
     * @param[reqObj] тело запроса.
     * @return возвращает экземпляр Request
     */
    private fun makeRequest(uri: String, reqObj: kotlin.Any): Request {
        val fullUrl = "$urlApi/$uri"
        return Request.Builder()
            .url(fullUrl)
            .post(
                RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    gsonBuilder.toJson(reqObj)
                ))
            .build()
    }

    /**
     * Производит login команды.
     *
     * Отправляет запрос к серверу, парсит данные и вызывает [callback] при завершении запроса.
     *
     * @param[login] логин команды.
     * @param[password] пароль команды.
     * @param[callback] вызывается при завершении запроса.
     */
    fun loginTeam(login: String, password: String, callback: (response: NetworkResponse, team: String) -> Unit) {
        val request = makeRequest("loginTeam",
            LoginTeamRequest(login = login, password = password)
        )
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val data = gsonBuilder.fromJson(body, LoginTeamResponse::class.java)
                if (data.message == "ok")
                    callback(NetworkResponse.OK, data.teamName)
                else
                    callback(NetworkResponse.ERROR, "")
            }

            override fun onFailure(call: Call, e: IOException) {
                callback(NetworkResponse.ERROR, "")
            }
        })
    }

    /**
     * Производит rename команды.
     *
     * Отправляет запрос к серверу, парсит данные и вызывает [callback] при завершении запроса.
     *
     * @param[login] логин команды.
     * @param[teamName] новое название команды.
     * @param[callback] вызывается при завершении запроса
     */
    fun renameTeam(login:String, teamName: String, callback: (response: NetworkResponse) -> Unit) {
        val request = makeRequest("renameTeam",
            RenameTeamRequest(login = login, teamName = teamName)
        )
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                Log.d(LOG_KEY, "response: $response")
                val body = response.body()?.string()
                val data = gsonBuilder.fromJson(body, RenameTeamResponse::class.java)
                callback(
                    when (data.message) {
                        "ok" -> NetworkResponse.OK
                        else -> NetworkResponse.ERROR
                    }
                )
            }

            override fun onFailure(call: Call, e: IOException) {
                callback(NetworkResponse.ERROR)
            }
        })
    }

    /**
     * Добавляет команду к участию в квесте с id [questId].
     *
     * @param[login] логин команды.
     * @param[questId] id текущего квеста.
     * @param[callback] вызывается при завершении запроса.
     */
    fun joinToQuest(login: String, questId: String, callback: (response: NetworkResponse) -> Unit) {
        val request = makeRequest("joinToQuest", JoinToQuestRequest(login = login, questId = questId))
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val data = gsonBuilder.fromJson(body, JoinToQuestResponse::class.java)
                callback(
                    when (data.message) {
                        "ok" -> NetworkResponse.OK
                        else -> NetworkResponse.ERROR
                    }
                )
            }

            override fun onFailure(call: Call, e: IOException) {
                callback(NetworkResponse.ERROR)
            }
        })
    }

    /**)
     * Убирает команду из участия в квесте с id [questId]
     *
     * @param[login] логин команды.
     * @param[questId] id текущего квеста.
     * @param[callback] вызывается при завершении запроса.
     */
    fun leaveQuest(login: String, questId: String, callback: (response: NetworkResponse) -> Unit) {
        val request = makeRequest("leaveQuest", LeaveQuestRequest(login = login, questId = questId))
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val data = gsonBuilder.fromJson(body, LeaveQuestResponse::class.java)
                callback(
                    when (data.message) {
                        "ok" -> NetworkResponse.OK
                        else -> NetworkResponse.ERROR
                    }
                )
            }

            override fun onFailure(call: Call, e: IOException) {
                callback(NetworkResponse.ERROR)
            }
        })
    }

    /**
     * Получает список всех квестов.
     *
     * @param[callback] вызывается при завершении запроса.
     */
    fun loadQuests(callback: (response: NetworkResponse, listOfQuests: List<Quest>) -> Unit) {
        val request = makeRequest("listOfQuests", "")
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val data = gsonBuilder.fromJson(body, ListOfQuestsResponse::class.java)
                when (data.message) {
                    "ok" -> callback(NetworkResponse.OK, data.listOfQuests)
                    else -> callback(NetworkResponse.ERROR, ArrayList())
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback(NetworkResponse.ERROR, ArrayList())
            }
        })
    }

    /**
     * Получает список заданий.
     *
     * @param[login] логин команды.
     * @param[questId] id текущего квеста.
     * @param[callback] вызывается при завершении запроса.
     */
    fun loadTasks(login: String, questId: String, callback: (response: NetworkResponse, listOfTasks: List<Task>) -> Unit) {
        val request = makeRequest("listOfTasks", ListOfTasksRequest(login = login, questId = questId))
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val data = gsonBuilder.fromJson(body, ListOfTasksResponse::class.java)
                when (data.message) {
                    "ok" -> callback(NetworkResponse.OK, data.tasks)
                    else -> callback(NetworkResponse.ERROR, ArrayList())
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback(NetworkResponse.ERROR, ArrayList())
            }
        })
    }

    /**
     * Получает текущее состояние.
     *
     * В случае успеха возвращаются такие данные как:
     *   * [Team.teamName]
     *   * [Quests.questId]
     *   * [Quests.step]
     *   * [Quests.times]
     *   * [Quests.timesComplete]
     *
     * @param[login] логин команды.
     * @param[callback] вызывается при завершении запроса.
     */
    fun getState(login: String, callback: (response: NetworkResponse, teamName: String, questId: String, step: Int,
                                           times: List<Int>, timesComplete: List<Int>) -> Unit) {
        val request = makeRequest("getState",
            GetStateRequest(login = login)
        )
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val data = gsonBuilder.fromJson(body, GetStateResponse::class.java)
                when (data.message) {
                    "ok" ->
                        callback(NetworkResponse.OK, data.teamName, data.questId, data.step, data.times, data.timesComplete)
                    else -> callback(NetworkResponse.ERROR, "", "", 0, ArrayList(), ArrayList())
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback(NetworkResponse.ERROR, "", "", 0, ArrayList(), ArrayList())
            }
        })
    }

    /**
     * Уведомляет о том, что задание [step] из квеста [questId] решено верно.
     *
     * @param[login] логин команды.
     * @param[questId] id квеста.
     * @param[step] позиция текущего задания.
     * @param[callback] вызывается при завершении запроса.
     */
    fun completeTask(login: String, questId: String, step: Int, callback: (response: NetworkResponse) -> Unit) {
        val request = makeRequest("completeTask",
            CompleteTaskRequest(login = login, questId = questId, taskNumber = step.toString()))
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val data = gsonBuilder.fromJson(body, CompleteTaskResponse::class.java)
                callback(
                    when (data.message) {
                        "ok" -> NetworkResponse.OK
                        else -> NetworkResponse.ERROR
                    }
                )
            }

            override fun onFailure(call: Call, e: IOException) {

            }
        })
    }

    /**
     * Запрещает создание более чем одного такого объекта.
     *
     * Глобальной точки доступа нету. При первом вызове newInstance возвращает ссылку на объект.
     */
    companion object {
        private const val LOG_KEY = "Network"
        private lateinit var instance: Network
        private const val url = "http://192.168.43.32:5000"
        private const val urlApi = "$url/api/v1.0"
        const val urlImg = "$url/quest_images/"

        fun newInstance(): Network? {
            if (::instance.isInitialized)
                return null
            instance = Network()
            return instance
        }
    }
}