package ru.spbgororient.cityorientation.questsController

import com.google.gson.annotations.SerializedName

data class LoginTeamRequest(
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String )

data class LoginTeamResponse(
    @SerializedName("message") val message: String,
    @SerializedName("team_name") val teamName: String )

data class RenameTeamRequest(
    @SerializedName("login") val login: String,
    @SerializedName("team_name") val teamName: String )

data class RenameTeamResponse(
    @SerializedName("message") val message: String)
