package ru.spbgororient.cityorientation.questsController

import com.google.gson.annotations.SerializedName

data class Quest(
    @SerializedName("quest_id") var questId: String,
    @SerializedName("name") var name: String,
    @SerializedName("place") var place: String,
    @SerializedName("date") var date: Int,
    @SerializedName("time") var time: Int,
    @SerializedName("amountOfCp") var amountOfCp: Int,
    @SerializedName("duration") var duration: Int)
