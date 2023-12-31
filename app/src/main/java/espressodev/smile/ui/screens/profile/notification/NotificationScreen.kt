package espressodev.smile.ui.screens.profile.notification

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import espressodev.smile.common.composables.PermissionPopUp
import espressodev.smile.data.datastore.DataStoreService.Companion.DISABLED
import espressodev.smile.data.datastore.DataStoreService.Companion.ENABLED
import espressodev.smile.domain.util.Constants.HIGH_PADDING
import espressodev.smile.domain.util.Constants.VERY_HIGH_PADDING
import espressodev.smile.domain.util.icons.NotificationIcon
import espressodev.smile.ui.screens.home.homeRoute
import espressodev.smile.R.string as AppText


@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NotificationRoute(
    clearAndNavigate: (String) -> Unit,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val notificationPanelState by viewModel.notificationPanelState.collectAsStateWithLifecycle()
    val permissionState =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    when (permissionState.status.isGranted) {
        true -> {
            viewModel.setNotificationsEnabled(ENABLED) {
                clearAndNavigate(homeRoute)
            }
        }

        false -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && notificationPanelState) {
                PermissionPopUp({ permissionState.launchPermissionRequest() }) {
                    viewModel.onNotificationPanelStateChange(false)
                }
                viewModel.setNotificationsEnabled(DISABLED)
            }
        }
    }

    NotificationScreen(
        onSkipClick = {
            viewModel.setNotificationsEnabled(DISABLED) {
                clearAndNavigate(homeRoute)
            }
        },
        onEnableClick = {
            viewModel.onNotificationPanelStateChange(true)
        }
    )
}

@Composable
fun NotificationScreen(onSkipClick: () -> Unit, onEnableClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(HIGH_PADDING)
            .imePadding(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = VERY_HIGH_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally,
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
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = HIGH_PADDING)
        ) {
            OutlinedButton(onClick = onSkipClick) { Text(stringResource(AppText.skip)) }
            Button(onClick = onEnableClick) { Text(stringResource(AppText.enable_notifications)) }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NotificationPreview() {
    NotificationScreen({}, {})
}