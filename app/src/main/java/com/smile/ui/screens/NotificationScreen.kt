package com.smile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.NotificationIcon
import com.smile.R.string as AppText


@Composable
fun NotificationScreenProvider() {

}

@Composable
fun NotificationScreen(onSkipClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(HIGH_PADDING)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = HIGH_PADDING)
        ) {
            Image(
                imageVector = NotificationIcon,
                contentDescription = stringResource(AppText.notification_permission)
            )
            Text(
                stringResource(AppText.notification_desc),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                minLines = 3
            )
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = {}) { Text(stringResource(AppText.skip)) }
            Button(onClick = {}) { Text(stringResource(AppText.enable_notifications)) }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NotificationPreview() {
    NotificationScreen({})
}