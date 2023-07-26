package com.smile.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.smile.util.Constants

@Composable
fun HeaderWrapper(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.padding(Constants.VERY_HIGH_PADDING),
        verticalArrangement = Arrangement.spacedBy(Constants.HIGH_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}

@Composable
fun FormWrapper(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.padding(
            start = Constants.VERY_MAX_PADDING,
            top = Constants.MEDIUM_PADDING,
            end = Constants.VERY_MAX_PADDING
        ),
        verticalArrangement = Arrangement.spacedBy(Constants.HIGH_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}