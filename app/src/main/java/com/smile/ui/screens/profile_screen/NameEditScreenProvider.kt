package com.smile.ui.screens.profile_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smile.common.composables.BottomButtonWithIcon
import com.smile.common.composables.DefaultTextField
import com.smile.common.composables.LoadingAnimationDialog
import com.smile.common.composables.NavigationTopAppBar
import com.smile.ui.view_models.NameEditScreenViewModel
import com.smile.util.Constants.HIGH_PADDING
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText

@Composable
fun NameEditScreenProvider(
    popUp: () -> Unit,
    viewModel: NameEditScreenViewModel = hiltViewModel()
) {
    val name by viewModel.name.collectAsStateWithLifecycle()
    val loadingState by viewModel.loadingState.collectAsStateWithLifecycle()
    if (loadingState) {
        LoadingAnimationDialog { viewModel.onLoadingStateChange(false) }
    }
    NameEditScreen(name = name, onNameChange = viewModel::onNameChanged, popUp = popUp,
        onUpdateClick = {
            viewModel.onUpdateClick(popUp)
        }
    )
}


@Composable
private fun NameEditScreen(
    name: String,
    onNameChange: (String) -> Unit,
    popUp: () -> Unit,
    onUpdateClick: () -> Unit
) {
    Scaffold(topBar = { NavigationTopAppBar(title = AppText.change_name, popUp) }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(HIGH_PADDING)
        ) {
            DefaultTextField(value = name, AppText.name, onNameChange)
            Spacer(modifier = Modifier.weight(1f))
            BottomButtonWithIcon(
                AppDrawable.outline_edit_24,
                AppText.edit_profile,
                AppText.update,
                onUpdateClick
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
private fun NameEditPreview() {
    NameEditScreen(name = "Jeremy Combs", onNameChange = {}, popUp = {}, onUpdateClick = {})
}
