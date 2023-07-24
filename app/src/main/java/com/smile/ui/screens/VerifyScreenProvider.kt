package com.smile.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.smile.common.composables.DefaultButton
import com.smile.common.composables.NavigationTopAppBar
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.Constants.MEDIUM_PADDING
import com.smile.util.Constants.VERY_MAX_PADDING
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText


@Composable
fun VerifyScreenProvider(popUp: () -> Unit) {

}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun VerifyScreen(popUp: () -> Unit, resendVerificationClick: () -> Unit) {
    Scaffold(topBar = { NavigationTopAppBar(AppText.verify_email, popUp) }
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(HIGH_PADDING).padding(bottom = VERY_MAX_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.padding(top = VERY_MAX_PADDING),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painterResource(id = AppDrawable.confirmed_bro), null)
                Text(
                    text = stringResource(id = AppText.verify_description),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Column() {
                Text(text = stringResource(id = AppText.no_worries), fontWeight = FontWeight.Medium)
                DefaultButton(AppText.resend_verification, resendVerificationClick)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun VerifyScreenPreview() {
    VerifyScreen({}, {})
}