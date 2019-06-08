package ru.spbgororient.cityorientation.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CityOrientationApi {
    @POST("signUp")
    fun signUp(@Body body: SignUpRequest): Call<SignUpResponse>

    @POST("renameTeam")
    fun renameTeam(@Body body: RenameTeamRequest): Call<RenameTeamResponse>

    @GET("quests")
    fun loadQuests(): Call<QuestsResponse>

    @POST("joinToQuest")
    fun joinToQuest(@Body body: JoinToQuestRequest): Call<JoinToQuestResponse>

    @POST("leaveQuest")
    fun leaveQuest(@Body body: LeaveQuestRequest): Call<LeaveQuestResponse>

    @POST("tasks")
    fun tasks(@Body body: TasksRequest): Call<TasksResponse>

    @POST("getState")
    fun getState(@Body body: GetStateRequest): Call<GetStateResponse>

    @POST("completeTask")
    fun completeTask(@Body body: CompleteTaskRequest): Call<CompleteTaskResponse>

    @POST("useTip")
    fun useTip(@Body body: UseTipRequest): Call<UseTipResponse>
}