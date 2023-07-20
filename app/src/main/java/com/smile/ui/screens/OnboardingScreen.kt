package com.smile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.smile.common.composables.DefaultButton
import com.smile.common.composables.DefaultOutlinedButton
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.Constants.IMAGE_SIZE
import com.smile.util.Constants.MAX_PADDING
import com.smile.util.Constants.MEDIUM_PADDING
import com.smile.util.Constants.NO_PADDING
import com.smile.util.Constants.SMALL_PADDING
import com.smile.util.Constants.VERY_MAX_PADDING
import java.util.Locale
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText

@Composable
fun OnBoardingScreen(popUpAndNavigate: (String) -> Unit = {}) {
    Column(modifier = Modifier.fillMaxSize()) {
        OnboardingBanner(modifier = Modifier.weight(0.5f))
        OnboardingButtons(
            modifier = Modifier.weight(0.5f),
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}

@Composable
fun OnboardingBanner(modifier: Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(NO_PADDING, NO_PADDING, VERY_MAX_PADDING, VERY_MAX_PADDING),
        color = MaterialTheme.colorScheme.surfaceTint,
        shadowElevation = SMALL_PADDING
    ) {
        val image = painterResource(id = AppDrawable.logo_white)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = image,
                contentDescription = "Logo",
                modifier = Modifier.size(IMAGE_SIZE)
            )
            Text(
                text = stringResource(id = AppText.app_name).uppercase(Locale.ROOT),
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = stringResource(id = AppText.onboarding_slogan),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

}

@Composable
fun OnboardingButtons(modifier: Modifier, onLoginClick: () -> Unit, onRegisterClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(VERY_MAX_PADDING),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = AppText.onboarding_title),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(Modifier.height(MEDIUM_PADDING))
        Text(
            text = stringResource(id = AppText.onboarding_subtitle),
            style = MaterialTheme.typography.titleMedium,
            minLines = 2,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(MAX_PADDING))
        Row(horizontalArrangement = Arrangement.spacedBy(HIGH_PADDING)) {
            DefaultButton(AppText.login, onLoginClick)
            DefaultOutlinedButton(AppText.register, onRegisterClick)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewOnboarding() {
    OnBoardingScreen()
}