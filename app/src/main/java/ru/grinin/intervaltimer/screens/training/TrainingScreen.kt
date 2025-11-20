package ru.grinin.intervaltimer.screens.training

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TrainingRoute(
    modifier: Modifier = Modifier,
    viewModel: TrainingViewModel = hiltViewModel()
) {

    val timer = viewModel.timer

    Log.d("test123", "TrainingRoute: $timer")

    TrainingScreen(modifier = modifier)
}

@Composable
fun TrainingScreen(modifier: Modifier = Modifier) {

}