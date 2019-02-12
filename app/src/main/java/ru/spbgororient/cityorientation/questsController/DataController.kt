package ru.spbgororient.cityorientation.questsController

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class DataController private constructor() {
    var listOfQuests: List<Quest> = ArrayList()
    var listOfTasks: List<Task> = ArrayList()
    var teamName: String = "Team Name"
    var login: String = "Login"
    var password: String = "Password"
    var questId: String = "Quest ID"
    var step: Int = 0
    var times: List<Int> = List(0){0}
    var timesComplete: List<Int> = List(0){0}

    var isLoadListOfQuests = false

    var url = "http://127.0.0.1/api/v1.0"
    var gsonBuilder = GsonBuilder().create()
    var client = OkHttpClient()

    private fun makeRequest(urlRequest: String, reqObj: kotlin.Any): Request {
        val fullUrl = "$url/$urlRequest"
        val request = Request.Builder()
            .url(fullUrl)
            .post(RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                gsonBuilder.toJson(reqObj)
            ))
            .build()
        return request
    }

    fun loginTeam(_login: String, _password: String, callback: (ok: Boolean) -> Unit) {
        val request = makeRequest("loginTeam", LoginTeamRequest(login=_login, password=_password))
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val data = gsonBuilder.fromJson(body, LoginTeamResponse::class.java)
                if (data.message == "ok") {
                    teamName = data.teamName
                    login = _login
                    password = _password
                    callback(true)
                } else
                    callback(false)
            }

            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }
        })
    }

    fun renameTeam(_teamName: String, callback: (ok: Boolean) -> Unit) {
        val request = makeRequest("renameTeam", RenameTeamRequest(login=login, teamName=_teamName))
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val data = gsonBuilder.fromJson(body, RenameTeamResponse::class.java)
                if (data.message == "ok") {
                    teamName = _teamName
                    callback(true)
                } else
                    callback(false)
            }

            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }
        })
    }

    fun loadQuests(callback: (ans: Boolean) -> Unit) {
        isLoadListOfQuests = false

        val fullUrl = "$url/listOfQuests"
        val request = Request.Builder()
            .url(fullUrl)
            .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "")).build()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val data = gsonBuilder.fromJson(body, ListOfQuestsResponse::class.java)
                if (data.message == "ok") {
                    listOfQuests = data.listOfQuests
                    isLoadListOfQuests = true
                    callback(true)
                } else
                    callback(false)
            }

            override fun onFailure(call: Call, e: IOException) {
                isLoadListOfQuests = true
                callback(false)
            }
        })
    }

    fun joinToQuest(position: Int, callback: (ans: Boolean) -> Unit) {
        val request = makeRequest("joinToQuest", JoinToQuestRequest(login=login, questId=listOfQuests[position].questId))
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val data = gsonBuilder.fromJson(body, JoinToQuestResponse::class.java)
                Log.d("joinToQuest", data.message)
                if (data.message == "ok") {
                    questId = listOfQuests[position].questId
                    callback(true)
                } else
                    callback(false)
            }

            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }
        })
    }

    fun listOfTasks(callback: (ans: Boolean) -> Unit) {
        val request = makeRequest("listOfTasks", ListOfTasksRequest(login=login, questId=questId))
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val data = gsonBuilder.fromJson(body, ListOfTasksResponse::class.java)
                Log.d("listOfTasks", data.toString())
                if (data.message == "ok") {
                    listOfTasks = data.tasks
                    callback(true)
                } else
                    callback(false)
            }

            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }
        })
    }

    fun getState(callback: (ans: Boolean) -> Unit) {
        val request = makeRequest("getState", GetStateRequest(login=login))
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val data = gsonBuilder.fromJson(body, GetStateResponse::class.java)
                Log.d("getState", data.toString())
                if (data.message == "ok") {
                    callback(true)
                    teamName = data.teamName
                    questId = data.questId
                    step = data.step
                    times = data.times
                    timesComplete = data.timesComplete
                } else
                    callback(false)
            }

            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }
        })
    }

    companion object {
        val instance: DataController by lazy { DataController() }
    }
}