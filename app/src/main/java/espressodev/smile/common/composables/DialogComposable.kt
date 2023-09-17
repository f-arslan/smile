package espressodev.smile.common.composables

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import espressodev.smile.common.animations.ThreeDotAnimation
import espressodev.smile.util.Constants.MEDIUM_PADDING
import kotlinx.coroutines.delay
import espressodev.smile.R.drawable as AppDrawable
import espressodev.smile.R.string as AppText

@Composable
fun VerificationDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MEDIUM_PADDING)
            ) {
                Icon(
                    painter = painterResource(id = AppDrawable.confirm_badge),
                    contentDescription = stringResource(id = AppText.confirm_icon)
                )
                Text(
                    text = stringResource(id = AppText.email_confirmation_body),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text(stringResource(AppText.close)) } }
    )
}

@Composable
fun LoadingAnimationDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = {}) {
        var buttonState by rememberSaveable {
            mutableStateOf(false)
        }
        LaunchedEffect(buttonState) {
            delay(3000L)
            buttonState = true
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ThreeDotAnimation()
            TextButton(onClick = onDismiss, enabled = buttonState) {
                Text(text = stringResource(AppText.cancel), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun FunctionalityNotAvailablePopup(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = stringResource(AppText.working_on),
                style = MaterialTheme.typography.titleMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = AppText.close))
            }
        }
    )
}

@Composable
fun PermissionPopUp(onRequestPermission: () -> Unit, permissionStateChange: (Boolean) -> Unit) {
    var showWarningDialog by remember { mutableStateOf(true) }

    if (showWarningDialog) {
        Popup(
            onDismissRequest = { showWarningDialog = false }
        ) {
            onRequestPermission()
            permissionStateChange(false)
        }
    }
}

@Composable
fun AppAlertDialog(
    @DrawableRes icon: Int,
    @StringRes title: Int,
    @StringRes text: Int,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        icon = {
            Icon(
                painter = painterResource(icon),
                contentDescription = stringResource(AppText.alert_icon)
            )
        },
        title = { Text(stringResource(title)) },
        text = { Text(stringResource(text), textAlign = TextAlign.Justify) },
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = onConfirm) { Text(stringResource(AppText.confirm)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(AppText.dismiss)) } })
}


@Composable
@Preview(showBackground = true)
fun DialogPreview() {
    AppAlertDialog(
        icon = AppDrawable.outline_info_24,
        title = AppText.delete_my_account,
        text = AppText.delete_confirm_message_dialog,
        onDismiss = {},
        onConfirm = {})
}