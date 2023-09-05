package com.smile.ui.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smile.common.composables.IconCircle
import com.smile.common.composables.LetterInCircle
import com.smile.common.composables.NavigationTopAppBar
import com.smile.model.User
import com.smile.model.datastore.DataStoreRepository.Companion.ENABLED
import com.smile.model.service.module.Response
import com.smile.ui.screens.graph.SmileRoutes.CHANGE_PASSWORD_SCREEN
import com.smile.ui.view_models.ProfileScreenViewModel
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.Constants.MEDIUM_PADDING
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText

@Composable
fun ProfileScreenProvider(
    popUp: () -> Unit,
    clearAndNavigate: (String) -> Unit,
    navigate: (String) -> Unit,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) { viewModel.getUserAndNotificationState() }
    val user by viewModel.user.collectAsStateWithLifecycle()
    val notificationState by viewModel.notificationState.collectAsStateWithLifecycle()
    if (user is Response.Success) {
        val letter = (user as Response.Success<User>).data.displayName.first()
        ProfileScreen(
            userLetter = letter,
            notificationState = notificationState,
            popUp = popUp,
            onEditProfileClick = { navigate(CHANGE_PASSWORD_SCREEN) },
            onApplicationInformationClick = {},
            signOutClick = { viewModel.signOut { clearAndNavigate(it) } },
            onNotificationActivateClick = {}
        )
    }
}


@Composable
fun ProfileScreen(
    userLetter: Char,
    notificationState: String,
    popUp: () -> Unit,
    onEditProfileClick: () -> Unit,
    onApplicationInformationClick: () -> Unit,
    signOutClick: () -> Unit,
    onNotificationActivateClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            NavigationTopAppBar(AppText.profile_screen, popUp)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(HIGH_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(HIGH_PADDING)
        ) {
            LetterInCircle(userLetter)
            Spacer(modifier = Modifier.height(HIGH_PADDING))
            ProfileItem(AppDrawable.outline_lock_24, AppText.change_password, onEditProfileClick)
            ProfileItem(
                AppDrawable.outline_info_24,
                AppText.app_info,
                onApplicationInformationClick
            )
            if (notificationState != ENABLED)
                ProfileItem(
                    AppDrawable.outline_notifications_active_24,
                    AppText.notification_active,
                    onNotificationActivateClick
                )
            ProfileItem(AppDrawable.round_logout_24, AppText.logout, signOutClick)
        }
    }
}

@Composable
fun ProfileItem(
    @DrawableRes leadingIcon: Int,
    @StringRes label: Int,
    onClick: () -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(HIGH_PADDING))
            .clickable {
                onClick()
            }
            .padding(MEDIUM_PADDING)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HIGH_PADDING)
        ) {
            IconCircle(leadingIcon, label)
            Text(stringResource(label), style = MaterialTheme.typography.titleMedium)
        }
        Icon(
            painter = painterResource(AppDrawable.outline_navigate_next_24),
            contentDescription = stringResource(AppText.next)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileScreen(
        userLetter = 'T',
        notificationState = "South Dakota",
        popUp = {},
        onEditProfileClick = {},
        onApplicationInformationClick = {},
        signOutClick = {},
        onNotificationActivateClick = {})
}