package ru.grinin.intervaltimer.screens.training

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.grinin.intervaltimer.R

@Composable
fun TrainingRoute(
    modifier: Modifier = Modifier,
    viewModel: TrainingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TrainingScreen(
        modifier = modifier,
        uiState = uiState,
        stopTraining = viewModel::stopTraining,
        startTraining = viewModel::startTraining
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingScreen(
    modifier: Modifier = Modifier,
    uiState: TrainingUiState,
    stopTraining: () -> Unit,
    startTraining: () -> Unit,
) {
    val timer = uiState.timer

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text(timer.title) })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (uiState.isRunning) stopTraining()
                    else startTraining()
                },
            ) {
                Icon(
                    imageVector = if (uiState.isRunning) Icons.Default.Clear else Icons.Default.PlayArrow,
                    contentDescription = null
                )
            }
        },
    ) { paddingValues ->
        TrainingContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState
        )
    }
}

@Composable
fun TrainingContent(
    modifier: Modifier = Modifier,
    uiState: TrainingUiState
) {
    var tabIndex by rememberSaveable { mutableStateOf(0) }

    Column(modifier = modifier) {
        TabRow(selectedTabIndex = tabIndex) {
            Tab(selected = tabIndex == 0, onClick = { tabIndex = 0 }, text = {
                Text(
                    stringResource(R.string.timer)
                )
            })
            Tab(selected = tabIndex == 1, onClick = { tabIndex = 1 }, text = {
                Text(
                    stringResource(R.string.map)
                )
            })
        }

        when (tabIndex) {
            0 -> TimerTab(uiState)
            1 -> MapTabPlaceholder()
        }
    }
}

@Composable
fun MapTabPlaceholder() {

}

@Composable
fun TimerTab(state: TrainingUiState) {
    val timer = state.timer
    val isRunning = state.isRunning
    val totalTime = timer.totalTime
    val currentIntervalIndex = state.currentIntervalIndex

    Column(
        Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Общее время: ${totalTime}s", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(8.dp))

        timer.intervals.forEachIndexed { index, interval ->
            val isCurrent = isRunning && index == currentIntervalIndex
            val progress = if (isCurrent && interval.time > 0)
                state.currentTimeLeft.toFloat() / interval.time
            else 1f

            Column {
                Text(interval.title)
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp),
                    color = if (isCurrent) Color.Green else Color.Gray,
                    trackColor = ProgressIndicatorDefaults.linearTrackColor,
                    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}