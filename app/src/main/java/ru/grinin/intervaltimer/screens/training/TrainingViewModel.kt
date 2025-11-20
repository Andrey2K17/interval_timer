package ru.grinin.intervaltimer.screens.training

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import ru.grinin.intervaltimer.entities.TimerUI
import ru.grinin.intervaltimer.service.TrainingService
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {
    private val timer: TimerUI = checkNotNull(savedStateHandle["timer"])

    private val _uiState = MutableStateFlow(TrainingUiState(timer))
    val uiState: StateFlow<TrainingUiState> = _uiState

    fun stopTraining() {
        _uiState.update { it.copy(isRunning = false) }
        TrainingService.stop(getApplication())
    }

    fun startTraining() {
        _uiState.update { it.copy(isRunning = true, isFinished = false) }
        TrainingService.start(getApplication(), timer)
    }

    fun updateProgress(index: Int, timeLeft: Int, isFinished: Boolean) {
        _uiState.update {
            it.copy(
                currentIntervalIndex = index,
                currentTimeLeft = timeLeft,
                isFinished = isFinished,
                isRunning = !isFinished
            )
        }
    }
}

data class TrainingUiState(
    val timer: TimerUI,
    val isRunning: Boolean = false,
    val isFinished: Boolean = false,
    val currentIntervalIndex: Int = 0,
    val currentTimeLeft: Int = 0
)