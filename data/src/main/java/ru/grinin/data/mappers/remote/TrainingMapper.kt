package ru.grinin.data.mappers.remote

import ru.grinin.data.entities.IntervalRemote
import ru.grinin.data.entities.TrainingRemote
import ru.grinin.domain.entities.IntervalDomain
import ru.grinin.domain.entities.TrainingDomain

fun IntervalRemote.toDomain() = IntervalDomain(duration)

fun TrainingRemote.toDomain() = TrainingDomain(id, intervals.map { it.toDomain() })