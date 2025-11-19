package ru.grinin.domain.entities

data class TrainingDomain(
    val id: Int,
    val intervals: List<IntervalDomain>
)