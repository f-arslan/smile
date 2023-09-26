package espressodev.smile.ui.screens.login.forgot_password

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import espressodev.smile.common.composables.DefaultTextField
import espressodev.smile.domain.util.Constants.HIGH_PADDING
import espressodev.smile.domain.util.Constants.MEDIUM_HIGH_PADDING
import espressodev.smile.domain.util.Constants.MEDIUM_PADDING
import espressodev.smile.domain.util.Constants.NO_SMALL_PADDING
import espressodev.smile.domain.util.Constants.SMALL_PADDING
import espressodev.smile.domain.util.Constants.VERY_MAX_PADDING
import espressodev.smile.domain.util.Constants.VERY_SMALL_PADDING
import espressodev.smile.ui.screens.login.LOGIN_GRAPH_ROUTE_PATTERN
import espressodev.smile.R.drawable as AppDrawable
import espressodev.smile.R.string as AppText

@Composable
fun ForgotPasswordRoute(
    clearAndNavigate: (String) -> Unit,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {
    val email by viewModel.email.collectAsStateWithLifecycle()
    ForgotPasswordScreen(
        email,
        viewModel::onEmailChange,
        onResetPasswordClick = {
            viewModel.sendPasswordResetEmail {
                clearAndNavigate(LOGIN_GRAPH_ROUTE_PATTERN)
            }
        },
        backToLoginClick = { clearAndNavigate(LOGIN_GRAPH_ROUTE_PATTERN) })
}

@Composable
fun ForgotPasswordScreen(
    email: String,
    onEmailChange: (String) -> Unit,
    onResetPasswordClick: () -> Unit,
    backToLoginClick: () -> Unit
) {
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(HIGH_PADDING),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FingerprintIcon()
            Spacer(Modifier.height(HIGH_PADDING))
            Text(
                stringResource(AppText.forgot_password),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(MEDIUM_PADDING))
            Text(stringResource(AppText.forgot_password_instruction), textAlign = TextAlign.Center)
            Spacer(Modifier.height(HIGH_PADDING))
            DefaultTextField(email, AppText.email, onEmailChange)
            Spacer(Modifier.height(HIGH_PADDING))
            Button(onClick = onResetPasswordClick, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(AppText.reset_password))
            }
            TextButton(onClick = backToLoginClick, modifier = Modifier.imePadding()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(SMALL_PADDING),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(AppDrawable.outline_arrow_back_24),
                        contentDescription = stringResource(AppText.back_arrow),
                        modifier = Modifier.size(HIGH_PADDING)
                    )
                    Text(
                        stringResource(AppText.back_login),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun FingerprintIcon() {
    Surface(
        shadowElevation = VERY_SMALL_PADDING,
        modifier = Modifier.size(VERY_MAX_PADDING),
        shape = RoundedCornerShape(MEDIUM_HIGH_PADDING),
        border = BorderStroke(
            NO_SMALL_PADDING,
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
    ) {
        Icon(
            painter = painterResource(AppDrawable.outline_fingerprint_24),
            contentDescription = stringResource(AppText.forgot_password),
            modifier = Modifier
                .padding(MEDIUM_PADDING)
                .rotate(15f)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ForgotPreview() {
    ForgotPasswordScreen(
        email = "albert.raymond@example.com",
        onEmailChange = {},
        onResetPasswordClick = {}, backToLoginClick = {})
}