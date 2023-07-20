package com.smile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smile.R.drawable as AppDrawable
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.Constants.IMAGE_SIZE
import com.smile.util.Constants.NO_PADDING

@Composable
fun OnBoardingScreen(popUpAndNavigate: (String) -> Unit = {}) {
    Column(modifier = Modifier.fillMaxSize()) {
        OnboardingBanner(modifier = Modifier.weight(0.5f))
        OnboardingButtons()
    }
}
@Composable
fun OnboardingBanner(modifier: Modifier) {
    val isDarkTheme = isSystemInDarkTheme()
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(NO_PADDING, NO_PADDING, HIGH_PADDING, HIGH_PADDING)
    ) {
        val image = if (isDarkTheme) {
            painterResource(id = AppDrawable.logo_white)
        } else {
            painterResource(id = AppDrawable.logo_black)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
             Image(painter = image, contentDescription = "Logo", modifier = Modifier.size(IMAGE_SIZE))
        }
    }

}

@Composable
fun OnboardingButtons() {

}


@Preview(showBackground = true)
@Composable
fun PreviewOnboarding() {
    OnBoardingScreen()
}