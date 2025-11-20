package ru.grinin.intervaltimer.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.grinin.intervaltimer.R
import ru.grinin.intervaltimer.entities.DialogInfo
import ru.grinin.intervaltimer.entities.UIState


@Composable
fun <State, Event> CollectStateWithEvents(
    stateFlow: StateFlow<State>,
    eventsFlow: Flow<UIState<Event>>,
    onSuccessEvent: (Event) -> Unit,
    onDismiss: () -> Unit = {},
    onConfirm: (DialogInfo) -> Unit = {},
    onSuccess: @Composable (State, Boolean) -> Unit,
) {
    var dialogErrorInfo by remember { mutableStateOf(DialogInfo.defaultDialog) }
    var eventLoading by remember { mutableStateOf(false) }
    val state by stateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        eventsFlow.collect { event ->
            event.doOnLoading { eventLoading = true }
                .doOnSuccess { onSuccessEvent(it) }
                .doOnError { dialogErrorInfo = it.info }
        }
    }

    onSuccess(state, eventLoading)

    DialogHandler(
        dialogErrorInfo = dialogErrorInfo,
        onDismiss = onDismiss,
        onConfirm = onConfirm
    ) { dialogErrorInfo = DialogInfo.defaultDialog }
}

@Composable
fun Loader(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = modifier,
            color = color
        )
    }
}

@Composable
private fun DialogHandler(
    dialogErrorInfo: DialogInfo,
    confirmButtonText: String = stringResource(id = R.string.general_confirm),
    dialogTitle: String = stringResource(id = R.string.general_error_title),
    onDismiss: () -> Unit,
    onConfirm: (DialogInfo) -> Unit,
    clearDialog: () -> Unit
) {
    if (dialogErrorInfo.isShow) {
        CustomAlertDialog(
            confirmButtonText = confirmButtonText,
            onDismissRequest = {
                onDismiss()
                clearDialog()
            },
            onConfirmation = {
                onConfirm(dialogErrorInfo)
                clearDialog()
            },
            dialogText = dialogErrorInfo.text.ifEmpty { stringResource(id = R.string.general_error_title) },
            icon = Icons.Default.Info,
            dialogTitle = dialogTitle,
        )
    }
}