package ru.grinin.intervaltimer.screens.training

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object TrainingProgressBus {
    private val _updates = MutableSharedFlow<TrainingUpdate>()
    val updates = _updates.asSharedFlow()

    suspend fun send(update: TrainingUpdate) {
        _updates.emit(update)
    }
}

data class TrainingUpdate(
    val intervalIndex: Int,
    val timeLeft: Int,
    val finished: Boolean
)