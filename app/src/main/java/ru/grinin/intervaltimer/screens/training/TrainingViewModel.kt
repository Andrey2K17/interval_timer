package ru.grinin.intervaltimer.screens.training

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.grinin.intervaltimer.entities.TrainingUI
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
): ViewModel() {
    val training: TrainingUI = checkNotNull(savedStateHandle["training"])

}