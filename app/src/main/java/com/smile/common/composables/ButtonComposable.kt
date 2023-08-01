package com.smile.common.composables

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText


@Composable
fun DefaultApp(@DrawableRes icon: Int, @StringRes contentDescription: Int, onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Image(
            painter = painterResource(icon),
            contentDescription = stringResource(id = contentDescription)
        )
    }
}

@Composable
fun DefaultButton(@StringRes text: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(modifier = modifier, onClick = onClick) {
        Text(text = stringResource(id = text))
    }
}

@Composable
fun DefaultOutlinedButton(@StringRes text: Int, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick) {
        Text(text = stringResource(id = text))
    }
}

@Composable
fun AppFloActionButton(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = AppDrawable.round_message_24),
            contentDescription = stringResource(AppText.message_icon)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonPreview() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AppFloActionButton({})
    }
}