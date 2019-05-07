package ru.spbgororient.cityorientation.quests

import com.google.gson.annotations.SerializedName

data class Quest(
    @SerializedName("quest_id") var questId: String,
    @SerializedName("name") var name: String,
    @SerializedName("place") var place: String,
    @SerializedName("img") var img: String,
    @SerializedName("start_time") var startTime: Long,
    @SerializedName("amount_of_cp") var amountOfCp: Int,
    @SerializedName("duration") var duration: Int,
    @SerializedName("start_text") var startText: String,
    @SerializedName("final_text") var finalText: String)
