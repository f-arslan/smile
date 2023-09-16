package com.smile.ui.screens.profile_screen

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
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
import com.smile.ui.screens.graph.SmileRoutes.DELETE_PROFILE_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.LEARN_MORE_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.NAME_EDIT_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.NOTIFICATION_SCREEN
import com.smile.ui.screens.graph.SmileRoutes.VERIFY_PASSWORD_SCREEN
import com.smile.ui.view_models.ProfileScreenViewModel
import com.smile.util.Constants.AVATAR_SIZE
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.Constants.MEDIUM_PADDING
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText

@Composable
fun ProfileScreenProvider(
    popUp: () -> Unit,
    clearAndNavigate: (String) -> Unit,
    navigate: (String) -> Unit,
    navigateWithArgument: (String, String) -> Unit,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) { viewModel.getUserAndNotificationState(context) }

    val user by viewModel.user.collectAsStateWithLifecycle()
    val notificationState by viewModel.notificationState.collectAsStateWithLifecycle()
    if (user is Response.Success) {
        ProfileScreen(
            userName = (user as Response.Success<User>).data.displayName,
            notificationState = notificationState,
            popUp = popUp,
            onChangePasswordClick = {
                navigateWithArgument(
                    VERIFY_PASSWORD_SCREEN,
                    CHANGE_PASSWORD_SCREEN
                )
            },
            onApplicationInformationClick = { navigate(LEARN_MORE_SCREEN) },
            signOutClick = { viewModel.signOut { clearAndNavigate(it) } },
            onNotificationActivateClick = { navigate(NOTIFICATION_SCREEN) },
            onEditClick = { navigate(NAME_EDIT_SCREEN) },
            onDeleteProfileClick = {
                if ((user as Response.Success<User>).data.email.isEmpty()) {
                    navigateWithArgument(
                        VERIFY_PASSWORD_SCREEN,
                        DELETE_PROFILE_SCREEN
                    )
                } else {
                    navigate(DELETE_PROFILE_SCREEN)
                }
            }
        )
    }
}


@Composable
fun ProfileScreen(
    userName: String,
    notificationState: String,
    popUp: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onApplicationInformationClick: () -> Unit,
    signOutClick: () -> Unit,
    onNotificationActivateClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteProfileClick: () -> Unit
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
            LetterInCircle(userName.first())
            UserName(userName, onEditClick)
            Spacer(modifier = Modifier.height(MEDIUM_PADDING))
            ProfileItem(AppDrawable.outline_lock_24, AppText.change_password, onChangePasswordClick)
            if (notificationState != ENABLED)
                ProfileItem(
                    AppDrawable.outline_notifications_active_24,
                    AppText.notification_active,
                    onNotificationActivateClick
                )
            ProfileItem(
                AppDrawable.outline_info_24,
                AppText.app_info,
                onApplicationInformationClick
            )
            ProfileItem(
                AppDrawable.outline_delete_24,
                AppText.delete_profile,
                onDeleteProfileClick
            )
            ProfileItem(AppDrawable.round_logout_24, AppText.logout, signOutClick)
        }
    }
}


@Composable
private fun UserName(userName: String, onEditClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.width(AVATAR_SIZE))
        Text(
            userName,
            style = MaterialTheme.typography.headlineLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        IconButton(onClick = onEditClick) {
            Icon(
                painter = painterResource(AppDrawable.outline_edit_24),
                contentDescription = stringResource(AppText.edit_profile)
            )
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
        userName = "Fatih",
        notificationState = "South Dakota",
        popUp = {},
        onChangePasswordClick = {},
        onApplicationInformationClick = {},
        signOutClick = {},
        onNotificationActivateClick = {}, onEditClick = {}, onDeleteProfileClick = {})
}