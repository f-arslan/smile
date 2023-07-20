package com.smile.common.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.smile.R


@Composable
fun DefaultButton(@StringRes text: Int, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = stringResource(id = text))
    }
}

@Composable
fun DefaultOutlinedButton(@StringRes text: Int, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick) {
        Text(text = stringResource(id = text))
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonPreview() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        DefaultOutlinedButton(R.string.login) {}
    }
}