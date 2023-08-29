package com.smile.ui.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.smile.common.composables.IconCircle
import com.smile.common.composables.LetterInCircle
import com.smile.common.composables.NavigationTopAppBar
import com.smile.model.User
import com.smile.util.Constants.HIGH_PADDING
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText

@Composable
fun ProfileScreenProvider() {

}


@Composable
fun ProfileScreen(popUp: () -> Unit, user: User) {
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
            LetterInCircle(user.displayName.first().uppercase())
            Spacer(modifier = Modifier.height(HIGH_PADDING))
            ProfileItem()
        }
    }
}

@Composable
fun ProfileItem(@DrawableRes leadingIcon: Int, @StringRes leadingIconDesc: Int, @StringRes label: Int, @DrawableRes trailingIcon: Int, @StringRes trailingIconDesc: Int) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HIGH_PADDING)
        ) {
            IconCircle(leadingIcon, leadingIconDesc)
            Text(stringResource(label), style = MaterialTheme.typography.titleMedium)
        }
        Icon(
            painter = painterResource(trailingIcon),
            contentDescription = stringResource(trailingIconDesc)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileScreen(
        popUp = {}, user = User(
            userId = "vim",
            displayName = "Simone Schultz",
            email = "myra.english@example.com",
            profilePictureUrl = "http://www.bing.com/search?q=deseruisse",
            isEmailVerified = false,
            contactIds = listOf(),
            roomIds = listOf(),
            fcmToken = "dis"
        )
    )
}