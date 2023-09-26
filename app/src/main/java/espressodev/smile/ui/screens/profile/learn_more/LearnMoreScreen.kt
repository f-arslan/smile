package espressodev.smile.ui.screens.profile.learn_more

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import espressodev.smile.common.composables.HyperlinkText
import espressodev.smile.common.composables.NavigationTopAppBar
import espressodev.smile.domain.util.Constants.GITHUB_DESC
import espressodev.smile.domain.util.Constants.GITHUB_LINK
import espressodev.smile.domain.util.Constants.HIGH_PADDING
import espressodev.smile.domain.util.Constants.MEDIUM_PADDING
import espressodev.smile.domain.util.Constants.PRIVACY_POLICY
import espressodev.smile.domain.util.Constants.PRIVACY_POLICY_LINK
import espressodev.smile.domain.util.Constants.PRIVACY_TERM_CONDITIONS
import espressodev.smile.domain.util.Constants.SMALL_ICON_SIZE
import espressodev.smile.domain.util.Constants.TERMS_CONDITIONS
import espressodev.smile.domain.util.Constants.TERMS_CONDITIONS_LINK
import espressodev.smile.ui.theme.Pacifico
import espressodev.smile.R.drawable as AppDrawable
import espressodev.smile.R.string as AppText

@Composable
fun LearnMoreRoute(popUp: () -> Unit) {
    Scaffold(topBar = { NavigationTopAppBar(AppText.learn_more, popUp) }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(HIGH_PADDING)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val logo = if (isSystemInDarkTheme()) AppDrawable.logo_white else AppDrawable.logo_black
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        MEDIUM_PADDING
                    )
                ) {
                    Image(
                        painter = painterResource(logo),
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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(AppText.app_description),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(HIGH_PADDING))
                HyperlinkText(fullText = GITHUB_DESC, mapOf(
                    GITHUB_DESC to GITHUB_LINK
                ))
            }
            Spacer(Modifier)
            Spacer(Modifier)
            HyperlinkText(
                fullText = PRIVACY_TERM_CONDITIONS, mapOf(
                    PRIVACY_POLICY to PRIVACY_POLICY_LINK,
                    TERMS_CONDITIONS to TERMS_CONDITIONS_LINK
                )
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LearnMorePreview() {
    LearnMoreRoute(popUp = {})
}