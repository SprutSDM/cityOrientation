package ru.spbgororient.cityorientation.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.spbgororient.cityorientation.api.*
import ru.spbgororient.cityorientation.quests.Quest
import ru.spbgororient.cityorientation.quests.Quests
import ru.spbgororient.cityorientation.quests.Task
import ru.spbgororient.cityorientation.team.Team


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
        FAILURE,
        NETWORK_ERROR
    }

    private val interceptor = HttpLoggingInterceptor()
    private val okHttp3client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    private val mRetrofit = Retrofit.Builder()
        .baseUrl(URL_API)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp3client)
        .build()

    init {
        interceptor.level = HttpLoggingInterceptor.Level.BODY
    }

    private val cityApi = mRetrofit.create(CityOrientationApi::class.java)

    /**
     * Производит login команды.
     *
     * Отправляет запрос к серверу, парсит данные и вызывает [callback] при завершении запроса.
     *
     * @param[login] логин команды.
     * @param[password] пароль команды.
     * @param[callback] вызывается при завершении запроса.
     */
    fun signUp(login: String, password: String, callback: (response: NetworkResponse, team: String) -> Unit) {
        cityApi.signUp(SignUpRequest(login = login, password = password)).enqueue(object: Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                val data = response.body()!!
                if (data.message == "ok")
                    callback(NetworkResponse.OK, data.teamName)
                else
                    callback(NetworkResponse.FAILURE, "")
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                callback(NetworkResponse.NETWORK_ERROR, "")
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
        cityApi.renameTeam(RenameTeamRequest(login = login, teamName = teamName)).enqueue(object: Callback<RenameTeamResponse> {
            override fun onResponse(call: Call<RenameTeamResponse>, response: Response<RenameTeamResponse>) {
                val data = response.body()!!
                callback(
                    when (data.message) {
                        "ok" -> NetworkResponse.OK
                        else -> NetworkResponse.FAILURE
                    }
                )
            }

            override fun onFailure(call: Call<RenameTeamResponse>, t: Throwable) {
                callback(NetworkResponse.NETWORK_ERROR)
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
        cityApi.joinToQuest(JoinToQuestRequest(login = login, questId = questId)).enqueue(object: Callback<JoinToQuestResponse> {
            override fun onResponse(call: Call<JoinToQuestResponse>, response: Response<JoinToQuestResponse>) {
                val data = response.body()!!
                callback(
                    when (data.message) {
                        "ok" -> NetworkResponse.OK
                        else -> NetworkResponse.FAILURE
                    }
                )
            }

            override fun onFailure(call: Call<JoinToQuestResponse>, t: Throwable) {
                callback(NetworkResponse.NETWORK_ERROR)
            }
        })
    }

    /**
     * Убирает команду из участия в квесте с id [questId]
     *
     * @param[login] логин команды.
     * @param[questId] id текущего квеста.
     * @param[callback] вызывается при завершении запроса.
     */
    fun leaveQuest(login: String, questId: String, callback: (response: NetworkResponse) -> Unit) {
        cityApi.leaveQuest(LeaveQuestRequest(login = login, questId = questId)).enqueue(object: Callback<LeaveQuestResponse> {
            override fun onResponse(call: Call<LeaveQuestResponse>, response: Response<LeaveQuestResponse>) {
                val data = response.body()!!
                callback(
                    when (data.message) {
                        "ok" -> NetworkResponse.OK
                        else -> NetworkResponse.FAILURE
                    }
                )
            }

            override fun onFailure(call: Call<LeaveQuestResponse>, t: Throwable) {
                callback(NetworkResponse.NETWORK_ERROR)
            }
        })
    }

    /**
     * Получает список всех квестов.
     *
     * @param[callback] вызывается при завершении запроса.
     */
    fun loadQuests(callback: (response: NetworkResponse, listOfQuests: List<Quest>) -> Unit) {
        cityApi.loadQuests().enqueue(object: Callback<QuestsResponse> {
            override fun onResponse(call: Call<QuestsResponse>, response: Response<QuestsResponse>) {
                val data = response.body()!!
                when (data.message) {
                    "ok" -> callback(NetworkResponse.OK, data.listOfQuests)
                    else -> callback(NetworkResponse.FAILURE, ArrayList())
                }
            }

            override fun onFailure(call: Call<QuestsResponse>, t: Throwable) {
                callback(NetworkResponse.NETWORK_ERROR, ArrayList())
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
        cityApi.tasks(TasksRequest(login = login, questId = questId)).enqueue(object: Callback<TasksResponse> {
            override fun onResponse(call: Call<TasksResponse>, response: Response<TasksResponse>) {
                val data = response.body()!!
                when (data.message) {
                    "ok" -> callback(NetworkResponse.OK, data.tasks)
                    else -> callback(NetworkResponse.FAILURE, ArrayList())
                }
            }

            override fun onFailure(call: Call<TasksResponse>, t: Throwable) {
                callback(NetworkResponse.NETWORK_ERROR, ArrayList())
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
     *   * [Quests.timesComplete]
     *
     * @param[login] логин команды.
     * @param[callback] вызывается при завершении запроса.
     */
    fun getState(login: String, callback: (response: NetworkResponse, data: GetStateResponse) -> Unit) {
        cityApi.getState(GetStateRequest(login = login)).enqueue(object: Callback<GetStateResponse> {
            override fun onResponse(call: Call<GetStateResponse>, response: Response<GetStateResponse>) {
                val data = response.body()!!
                when (data.message) {
                    "ok" -> callback(NetworkResponse.OK, data)
                    else -> callback(NetworkResponse.FAILURE, GetStateResponse("", "", "", ArrayList(), 0, 0, 0, ArrayList()))
                }
            }

            override fun onFailure(call: Call<GetStateResponse>, t: Throwable) {
                callback(NetworkResponse.NETWORK_ERROR, GetStateResponse("", "", "", ArrayList(), 0, 0, 0, ArrayList()))
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
    fun completeTask(login: String, questId: String, step: Int, callback: (response: NetworkResponse, timeComplete: Int) -> Unit) {
        cityApi.completeTask(CompleteTaskRequest(login = login, questId = questId, taskNumber = step.toString()))
            .enqueue(object: Callback<CompleteTaskResponse> {
                override fun onResponse(call: Call<CompleteTaskResponse>, response: Response<CompleteTaskResponse>) {
                    val data = response.body()!!
                    when (data.message) {
                        "ok" -> callback(NetworkResponse.OK, data.timeComplete)
                        "task already complete" -> callback(NetworkResponse.OK, data.timeComplete)
                        else -> callback(NetworkResponse.FAILURE, 0)
                    }
                }

                override fun onFailure(call: Call<CompleteTaskResponse>, t: Throwable) {
                    callback(NetworkResponse.NETWORK_ERROR, 0)
                }
        })
    }

    /**
     * Уведомляет о том, что была использована подсказка под номером [tipNumber].
     *
     * @param[login] логин команды.
     * @param[questId] id квеста.
     * @param[step] номер задания.
     * @param[tipNumber] номер подсказки.
     * @param[callback] вызывается при завершении запроса.
     */
    fun useTip(login: String, questId: String, step: Int, tipNumber: Int, callback: (response: NetworkResponse) -> Unit) {
        cityApi.useTip(UseTipRequest(login = login, questId = questId, taskNumber = step.toString(), tipNumber = tipNumber.toString()))
            .enqueue(object: Callback<UseTipResponse> {
                override fun onResponse(call: Call<UseTipResponse>, response: Response<UseTipResponse>) {
                    val data = response.body()!!
                    when (data.message) {
                        "ok" -> callback(NetworkResponse.OK)
                        else -> callback(NetworkResponse.FAILURE)
                    }
                }

                override fun onFailure(call: Call<UseTipResponse>, t: Throwable) {
                    callback(NetworkResponse.NETWORK_ERROR)
                }
            })
    }

    companion object {
        private const val LOG_KEY = "Network"
        val instance: Network by lazy { Network() }
        const val URL = "http://192.168.42.207:5000/"
        private const val URL_API = "${URL}api/v1/"
        const val URL_IMG = "${URL}quest_images/"
    }
}