package com.smile.common.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.smile.common.animations.ThreeDotAnimation
import com.smile.util.Constants.MEDIUM_PADDING
import kotlinx.coroutines.delay
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText

@Composable
fun VerificationDialog(@StringRes text: Int, onDismiss: () -> Unit) {
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
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(AppText.cancel), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun DialogPreview() {
    LoadingAnimationDialog(onDismiss = {})
}