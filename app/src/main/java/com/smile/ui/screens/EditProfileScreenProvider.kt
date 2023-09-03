package com.smile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smile.common.composables.DefaultTextField
import com.smile.common.composables.NavigationTopAppBar
import com.smile.common.composables.PasswordTextField
import com.smile.ui.view_models.EditScreenUiState
import com.smile.ui.view_models.EditScreenViewModel
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.Constants.MEDIUM_PADDING
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditProfileScreenProvider(popUp: () -> Unit, viewModel: EditScreenViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    EditProfileScreen(
        uiState,
        popUp,
        viewModel::onNameChange,
        viewModel::onPasswordChange,
        viewModel::onRePasswordChange,
        onUpdateClick = {
            viewModel.onUpdateClick()
            keyboardController?.hide()
        }
    )
}


@Composable
fun EditProfileScreen(
    uiState: EditScreenUiState,
    popUp: () -> Unit,
    onNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRePasswordChange: (String) -> Unit,
    onUpdateClick: () -> Unit,
) {
    Scaffold(topBar = { NavigationTopAppBar(AppText.edit_profile, popUp) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(HIGH_PADDING), verticalArrangement = Arrangement.spacedBy(HIGH_PADDING)
        ) {
            DefaultTextField(uiState.name, AppText.name, onNameChange)
            PasswordTextField(uiState.password, AppText.password, onPasswordChange)
            PasswordTextField(uiState.rePassword, AppText.re_password, onRePasswordChange)
            Spacer(Modifier.weight(1f))
            Button(
                onClick = onUpdateClick, modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(MEDIUM_PADDING),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(AppDrawable.outline_edit_24),
                        contentDescription = stringResource(AppText.edit_profile)
                    )
                    Text(
                        stringResource(AppText.update),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EditProfilePreview() {
    EditProfileScreen(uiState = EditScreenUiState(
        name = "Concetta Cain",
        password = "congue",
        rePassword = "no",
        loadingState = false
    ),
        onNameChange = {},
        onPasswordChange = {},
        onRePasswordChange = {},
        popUp = {},
        onUpdateClick = {})
}