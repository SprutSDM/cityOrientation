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

data class ListOfQuestsResponse(
    @SerializedName("message") val message: String,
    @SerializedName("list_of_quests") val listOfQuests: List<Quest>)

data class JoinToQuestRequest(
    @SerializedName("login") val login: String,
    @SerializedName("quest_id") val questId: String)

data class JoinToQuestResponse(
    @SerializedName("message") val message: String)

data class LeaveQuestRequest(
    @SerializedName("login") val login: String,
    @SerializedName("quest_id") val questId: String)

data class LeaveQuestResponse(
    @SerializedName("message") val message: String)

data class ListOfTasksRequest(
    @SerializedName("login") val login: String,
    @SerializedName("quest_id") val questId: String)

data class ListOfTasksResponse(
    @SerializedName("message") val message: String,
    @SerializedName("tasks") val tasks: List<Task>)

data class GetStateRequest(
    @SerializedName("login") val login: String)

data class GetStateResponse(
    @SerializedName("message") val message: String,
    @SerializedName("quest_id") val questId: String,
    @SerializedName("team_name") val teamName: String,
    @SerializedName("times") val times: List<Int>,
    @SerializedName("times_complete") val timesComplete: List<Int>,
    @SerializedName("step") val step: Int,
    @SerializedName("date_now") val dateNow: Int,
    @SerializedName("time_now") val timeNow: Int)
