package espressodev.smile.ui.screens.profile_screen.delete_profile_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import espressodev.smile.common.composables.AppAlertDialog
import espressodev.smile.common.composables.LoadingAnimationDialog
import espressodev.smile.common.composables.NavigationTopAppBar
import espressodev.smile.domain.util.Constants.HIGH_PADDING
import espressodev.smile.domain.util.Constants.MEDIUM_PADDING
import espressodev.smile.R.drawable as AppDrawable
import espressodev.smile.R.string as AppText

@Composable
fun DeleteProfileScreenProvider(
    popUp: () -> Unit,
    clearAndNavigate: (String) -> Unit,
    viewModel: DeleteProfileScreenViewModel = hiltViewModel()
) {
    val alertDialogState by viewModel.alertDialogState.collectAsStateWithLifecycle()
    val loadingState by viewModel.loadingState.collectAsStateWithLifecycle()
    if (loadingState)
        LoadingAnimationDialog { viewModel.onLoadingStateChange(false) }
    if (alertDialogState) {
        AppAlertDialog(
            AppDrawable.outline_info_24,
            AppText.delete_my_account,
            AppText.delete_confirm_message_dialog,
            onDismiss = { viewModel.onAlertDialogStateChange(false) },
            onConfirm = { viewModel.onDeleteClick(clearAndNavigate) }
        )
    }
    DeleteProfileScreen(popUp, onDeleteClick = { viewModel.onAlertDialogStateChange(true) })
}


@Composable
private fun DeleteProfileScreen(popUp: () -> Unit, onDeleteClick: () -> Unit) {
    val scrollState = rememberScrollState()
    Scaffold(topBar = { NavigationTopAppBar(AppText.delete_profile, popUp) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(HIGH_PADDING)
                .verticalScroll(scrollState)
        ) {
            Image(
                painter = painterResource(AppDrawable.delete_profile),
                contentDescription = stringResource(AppText.delete_profile)
            )
            Text(stringResource(AppText.delete_confirm_sub))
            Text(
                stringResource(AppText.after_deleting_happening_list),
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(MEDIUM_PADDING))
            Text(
                stringResource(AppText.important_cons),
                style = MaterialTheme.typography.titleMedium
            )
            Text(stringResource(AppText.important_cons_list))
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.height(HIGH_PADDING))
            OutlinedButton(onClick = onDeleteClick, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(AppText.delete_my_account))
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
private fun DeleteProfileScreenPreview() {
    DeleteProfileScreen(popUp = {}, onDeleteClick = {})
}