package ru.grinin.intervaltimer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.grinin.domain.entities.NetworkResult
import ru.grinin.domain.entities.TrainingDomain
import ru.grinin.domain.usecases.GetTrainingUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTrainingUseCase: GetTrainingUseCase,
): ViewModel() {
    val flow: MutableStateFlow<NetworkResult<TrainingDomain>?> = MutableStateFlow(null)

    init {
        viewModelScope.launch {
            getTrainingUseCase(68.toString()).collect {
                Log.d("test123", "result: $it")
                flow.emit(it)
            }
        }
    }
}