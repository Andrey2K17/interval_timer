package ru.grinin.intervaltimer.screens.training

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    init {
        observeProgress()
    }

    fun stopTraining() {
        _uiState.update { it.copy(isRunning = false) }
        TrainingService.stop(getApplication())
    }

    fun startTraining() {
        _uiState.update { it.copy(isRunning = true, isFinished = false) }
        TrainingService.start(getApplication(), timer)
    }

    private fun observeProgress() {
        viewModelScope.launch {
            TrainingProgressBus.updates.collect { update ->
                updateProgress(update)
            }
        }
    }

    private fun updateProgress(update: TrainingUpdate) {
        _uiState.update {
            it.copy(
                currentIntervalIndex = update.intervalIndex,
                currentTimeLeft = update.timeLeft,
                isFinished = update.finished,
                isRunning = !update.finished
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