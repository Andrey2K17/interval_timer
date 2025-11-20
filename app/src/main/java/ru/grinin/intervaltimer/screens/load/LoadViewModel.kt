package ru.grinin.intervaltimer.screens.load

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.grinin.domain.usecases.GetTrainingUseCase
import ru.grinin.intervaltimer.entities.TimerUI
import ru.grinin.intervaltimer.entities.UIState
import ru.grinin.intervaltimer.entities.toUI
import ru.grinin.intervaltimer.mappers.mapToUIState
import javax.inject.Inject


@HiltViewModel
class LoadViewModel @Inject constructor(
    private val getTrainingUseCase: GetTrainingUseCase,
): ViewModel() {

    private val _training: MutableSharedFlow<UIState<TimerUI>> = MutableSharedFlow()
    val training = _training.asSharedFlow()

    private val _id: MutableStateFlow<String> = MutableStateFlow("")
    val id = _id.asStateFlow()

    init {

    }

    fun setId(id: String) {
        viewModelScope.launch { _id.emit(id) }
    }

    fun getTraining() {
        getTrainingUseCase(id.value)
            .mapToUIState { it.toUI() }
            .onEach { _training.emit(it) }
            .launchIn(viewModelScope)
    }
}