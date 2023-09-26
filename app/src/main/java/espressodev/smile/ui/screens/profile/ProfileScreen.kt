package espressodev.smile.ui.screens.profile

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
import espressodev.smile.common.composables.IconCircle
import espressodev.smile.common.composables.LetterInCircle
import espressodev.smile.common.composables.NavigationTopAppBar
import espressodev.smile.data.datastore.DataStoreService.Companion.ENABLED
import espressodev.smile.data.service.model.PROVIDER
import espressodev.smile.data.service.model.Response
import espressodev.smile.data.service.model.User
import espressodev.smile.domain.util.Constants.AVATAR_SIZE
import espressodev.smile.domain.util.Constants.HIGH_PADDING
import espressodev.smile.domain.util.Constants.MEDIUM_PADDING
import espressodev.smile.ui.screens.profile.change_password.changePasswordRoute
import espressodev.smile.ui.screens.profile.delete_profile.deleteProfileRoute
import espressodev.smile.ui.screens.profile.edit_name.editNameRoute
import espressodev.smile.ui.screens.profile.learn_more.learnMoreRoute
import espressodev.smile.ui.screens.profile.notification.notificationRoute
import espressodev.smile.ui.screens.profile.verify_password.verifyPasswordRoute
import espressodev.smile.R.drawable as AppDrawable
import espressodev.smile.R.string as AppText

@Composable
fun ProfileRoute(
    popUp: () -> Unit,
    clearAndNavigate: (String) -> Unit,
    navigate: (String) -> Unit,
    navigateWithArgument: (String, String) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) { viewModel.getUserAndNotificationState(context) }

    val user by viewModel.user.collectAsStateWithLifecycle()
    val notificationState by viewModel.notificationState.collectAsStateWithLifecycle()
    if (user is Response.Success) {
        ProfileScreen(
            user = (user as Response.Success<User>).data,
            notificationState = notificationState,
            popUp = popUp,
            onChangePasswordClick = {
                navigateWithArgument(
                    verifyPasswordRoute,
                    changePasswordRoute
                )
            },
            onApplicationInformationClick = { navigate(learnMoreRoute) },
            signOutClick = { viewModel.signOut { clearAndNavigate(it) } },
            onNotificationActivateClick = { navigate(notificationRoute) },
            onEditClick = { navigate(editNameRoute) },
            onDeleteProfileClick = {
                if ((user as Response.Success<User>).data.email.isEmpty()) {
                    navigateWithArgument(
                        verifyPasswordRoute,
                        deleteProfileRoute
                    )
                } else {
                    navigate(deleteProfileRoute)
                }
            }
        )
    }
}


@Composable
fun ProfileScreen(
    user: User,
    notificationState: String,
    popUp: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onApplicationInformationClick: () -> Unit,
    signOutClick: () -> Unit,
    onNotificationActivateClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteProfileClick: () -> Unit,
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
            LetterInCircle(user.displayName.first())
            UserName(user.displayName, onEditClick)
            Spacer(modifier = Modifier.height(MEDIUM_PADDING))
            if (user.provider == PROVIDER.EMAIL)
                ProfileItem(
                    AppDrawable.outline_lock_24,
                    AppText.change_password,
                    onChangePasswordClick
                )
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
        user = User(),
        notificationState = "South Dakota",
        popUp = {},
        onChangePasswordClick = {},
        onApplicationInformationClick = {},
        signOutClick = {},
        onNotificationActivateClick = {},
        onEditClick = {},
        onDeleteProfileClick = {},
    )
}