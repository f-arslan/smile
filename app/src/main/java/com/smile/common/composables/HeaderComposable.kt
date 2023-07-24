package com.smile.common.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.Constants.VERY_HIGH_PADDING
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText


@Composable
fun RegisterHeader() {
    Column(
        modifier = Modifier.padding(VERY_HIGH_PADDING),
        verticalArrangement = Arrangement.spacedBy(HIGH_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(AppText.register_header),
            style = MaterialTheme.typography.displaySmall
        )
        Text(
            text = stringResource(AppText.register_sub_header),
            minLines = 3,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationTopAppBar(@StringRes title: Int, popUp: () -> Unit) {
    Column {
    TopAppBar(
        title = { Text(text = stringResource(id = title), fontWeight = FontWeight.Medium) },
        navigationIcon = {
            IconButton(
                onClick = popUp) {
                Icon(
                    painter = painterResource(id = AppDrawable.outline_arrow_circle_left_24),
                    contentDescription = null
                )
            }
        })
    }
}


@Composable
@Preview
fun HeaderPreview() {
    NavigationTopAppBar(AppText.verify_email, {})
}