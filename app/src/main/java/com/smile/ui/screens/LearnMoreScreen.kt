package com.smile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.smile.common.composables.NavigationTopAppBar
import com.smile.ui.theme.Pacifico
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.Constants.MEDIUM_PADDING
import com.smile.util.Constants.SMALL_ICON_SIZE
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText

@Composable
fun LearnMoreScreen(popUp: () -> Unit) {
    Scaffold(topBar = { NavigationTopAppBar(AppText.learn_more, popUp) }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(HIGH_PADDING)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        MEDIUM_PADDING
                    )
                ) {
                    Image(
                        painter = painterResource(AppDrawable.logo_black),
                        contentDescription = stringResource(AppText.logo),
                        modifier = Modifier.size(SMALL_ICON_SIZE)
                    )
                    Text(
                        text = stringResource(AppText.app_name),
                        style = MaterialTheme.typography.displayLarge,
                        fontFamily = Pacifico,
                    )
                }
                Text(
                    text = stringResource(AppText.onboarding_slogan),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                text = stringResource(AppText.app_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LearnMorePreview() {
    LearnMoreScreen(popUp = {})
}