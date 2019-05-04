package ru.spbgororient.cityorientation.api

import com.google.gson.annotations.SerializedName
import ru.spbgororient.cityorientation.quests.Quest
import ru.spbgororient.cityorientation.quests.Task

data class SignUpRequest(
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String)

data class SignUpResponse(
    @SerializedName("message") val message: String,
    @SerializedName("team_name") val teamName: String)

data class RenameTeamRequest(
    @SerializedName("login") val login: String,
    @SerializedName("team_name") val teamName: String)

data class RenameTeamResponse(
    @SerializedName("message") val message: String)

data class QuestsResponse(
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

data class TasksRequest(
    @SerializedName("login") val login: String,
    @SerializedName("quest_id") val questId: String)

data class TasksResponse(
    @SerializedName("message") val message: String,
    @SerializedName("tasks") val tasks: List<Task>)

data class GetStateRequest(
    @SerializedName("login") val login: String)

data class GetStateResponse(
    @SerializedName("message") val message: String,
    @SerializedName("quest_id") val questId: String,
    @SerializedName("team_name") val teamName: String,
    @SerializedName("times_complete") val timesComplete: MutableList<Int>,
    @SerializedName("step") val step: Int,
    @SerializedName("seconds") val seconds: Long,
    @SerializedName("time_zone") val timeZone: Long)

data class CompleteTaskRequest(
    @SerializedName("login") val login: String,
    @SerializedName("quest_id") val questId: String,
    @SerializedName("task_number") val taskNumber: String)

data class CompleteTaskResponse(
    @SerializedName("message") val message: String,
    @SerializedName("time_complete") val timeComplete: Int)
