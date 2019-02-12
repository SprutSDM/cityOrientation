package ru.spbgororient.cityorientation.questsController

import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class DataController private constructor() {
    var listOfQuests: MutableList<Quest> = ArrayList()
    var teamName: String = "Team Name"
    var login: String = "Login"
    var password: String = "Password"

    var isLoadListOfQuests = false

    var url = "http://127.0.0.1:5000/api/v1.0"
    var gsonBuilder = GsonBuilder().create()
    var client = OkHttpClient()

    fun loadQuests(callback: () -> Unit) {
        listOfQuests.clear()
        isLoadListOfQuests = false

        var quest1 = Quest(name="Ориентирование", date="24.02.12", time="18:00", place="Выборгский район", amountOfCp=14)
        listOfQuests.add(quest1)

        var quest2 = Quest(name="Спортивное ориентирование", date="02.08.19", time="17:00", place="Петроградка район", amountOfCp=112)
        listOfQuests.add(quest2)

        isLoadListOfQuests = true
    }

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
                val body = response?.body()?.string()
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
                val body = response?.body()?.string()
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



    companion object {
        val instance: DataController by lazy { DataController() }
    }
}