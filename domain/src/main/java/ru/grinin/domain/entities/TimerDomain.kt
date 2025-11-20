package ru.grinin.domain.entities


data class TimerDomain(
    val timerId: Int,
    val title: String,
    val totalTime: Int,
    val intervals: List<IntervalDomain>
)