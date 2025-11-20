package ru.grinin.data.mappers.remote

import ru.grinin.data.entities.IntervalRemote
import ru.grinin.data.entities.TimerResponse
import ru.grinin.domain.entities.IntervalDomain
import ru.grinin.domain.entities.TimerDomain


fun TimerResponse.toDomain() = TimerDomain(
    timerId = this.timer.timerId,
    title = this.timer.title,
    totalTime = this.timer.totalTime,
    intervals = this.timer.intervals.map { it.toDomain() }
)

fun IntervalRemote.toDomain() = IntervalDomain(
    title = title, time = time,
)