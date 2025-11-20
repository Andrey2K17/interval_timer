package ru.grinin.data.entities

import com.google.gson.annotations.SerializedName

data class TimerResponse(
    val timer: TimerRemote
)

data class TimerRemote(
    @SerializedName("timer_id")
    val timerId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("total_time")
    val totalTime: Int,
    @SerializedName("intervals")
    val intervals: List<IntervalRemote>
)