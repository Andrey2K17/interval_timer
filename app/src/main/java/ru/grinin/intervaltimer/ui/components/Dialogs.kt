package ru.grinin.intervaltimer.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.grinin.intervaltimer.R

//region elements
@Composable
fun CustomAlertDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String = stringResource(id = R.string.general_error_title),
    dialogText: String,
    confirmButtonText: String = stringResource(id = R.string.general_confirm),
    icon: ImageVector = Icons.Default.Info,
) {

    AlertDialog(
        modifier = modifier.padding(vertical = 32.dp),
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(
                text = dialogTitle,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                text = dialogText,
                style = MaterialTheme.typography.bodyLarge.merge(
                    textAlign = TextAlign.Center,
                )
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(
                    text = confirmButtonText,
                    style = TextStyle(color = Color.Black)
                )
            }
        }
    )
}
//endregion

//region preview
@Preview(showBackground = true)
@Composable
fun CustomAlertDialogPreview() {
        CustomAlertDialog(
            onDismissRequest = { },
            onConfirmation = { },
            dialogText = "Test dialog text",
            icon = Icons.Default.Info
        )
}
//endregion