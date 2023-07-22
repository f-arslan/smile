package com.smile.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.Constants.VERY_HIGH_PADDING
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
