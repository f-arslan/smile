package espressodev.smile.common.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import espressodev.smile.R.drawable as AppDrawable
import espressodev.smile.R.string as AppText


@Composable
fun RegisterHeader() {
    HeaderWrapper {
        Text(
            text = stringResource(AppText.register_header),
            style = MaterialTheme.typography.displaySmall
        )
        Text(
            text = stringResource(AppText.register_sub_header),
            minLines = 2,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoginHeader() {
    HeaderWrapper {
        Text(
            text = stringResource(AppText.login_header),
            style = MaterialTheme.typography.displaySmall
        )
        Text(
            text = stringResource(AppText.login_sub_header),
            style = MaterialTheme.typography.titleMedium,
            minLines = 1,
            textAlign = TextAlign.Center
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationTopAppBar(@StringRes title: Int, popUp: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(id = title), fontWeight = FontWeight.Medium) },
        navigationIcon = {
            IconButton(
                onClick = popUp
            ) {
                Icon(
                    painter = painterResource(id = AppDrawable.outline_arrow_circle_left_24),
                    contentDescription = null
                )
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactTopAppBar(contactName: String, popUp: () -> Unit, onMoreClick: () -> Unit) {
    Column {
        TopAppBar(
            title = { Text(text = contactName, fontWeight = FontWeight.Medium) },
            navigationIcon = {
                IconButton(
                    onClick = popUp
                ) {
                    Icon(
                        painter = painterResource(id = AppDrawable.outline_arrow_circle_left_24),
                        contentDescription = null
                    )
                }
            },
            actions = {
                IconButton(onClick = onMoreClick) {
                    Icon(
                        painter = painterResource(id = AppDrawable.baseline_more_vert_24),
                        contentDescription = stringResource(id = AppText.options)
                    )
                }
            }
        )
        Divider()
    }
}

@Composable
fun ContactTopAppBar(
    query: String,
    onValueChange: (String) -> Unit,
    popUp: () -> Unit,
) {
    Column {
        NavigationTopAppBar(title = AppText.new_conversation, popUp)
        ContactTextField(query, onValueChange)
        Divider()
    }
}

@Composable
@Preview(showBackground = true)
fun HeaderPreview() {
    ContactTopAppBar(contactName = "Lenore Raymond", popUp = {}) {}
}