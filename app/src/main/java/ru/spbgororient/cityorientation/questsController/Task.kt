package ru.spbgororient.cityorientation.questsController

import com.google.gson.annotations.SerializedName

data class Task (
    @SerializedName("task_id") val taskId: String,
    @SerializedName("img") val img: String,
    @SerializedName("content") val content: String,
    @SerializedName("answers") val answers: List<String>,
    @SerializedName("time") val time: Int,
    @SerializedName("time_tips") val timeTips: List<Int>,
    @SerializedName("tips") val tips: List<String>
)