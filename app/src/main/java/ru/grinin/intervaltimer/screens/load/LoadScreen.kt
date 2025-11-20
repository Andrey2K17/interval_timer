package ru.grinin.intervaltimer.screens.load

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.grinin.intervaltimer.R
import ru.grinin.intervaltimer.entities.TimerUI
import ru.grinin.intervaltimer.ui.components.CollectStateWithEvents

@Composable
fun LoadRoute(
    modifier: Modifier = Modifier,
    viewModel: LoadViewModel = hiltViewModel(),
    navToTraining: (TimerUI) -> Unit,
) {
    CollectStateWithEvents(
        stateFlow = viewModel.id,
        eventsFlow = viewModel.training,
        onSuccessEvent = { navToTraining(it) },
        onSuccess = { state, isLoading ->
            LoadScreen(
                modifier = modifier,
                id = state,
                onSetId = viewModel::setId,
                onClick = viewModel::getTraining,
                isLoading = isLoading,
            )
        }
    )
}

@Composable
fun LoadScreen(
    modifier: Modifier = Modifier,
    id: String,
    onSetId: (String) -> Unit,
    onClick: () -> Unit,
    isLoading: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = id,
            onValueChange = onSetId,
        )

        Spacer(Modifier.size(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
            enabled = !isLoading && id.isNotBlank(),
            onClick = onClick,
        ) {
            if(isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Text(
                    text = stringResource(R.string.load),
                )
            }
        }
    }
}