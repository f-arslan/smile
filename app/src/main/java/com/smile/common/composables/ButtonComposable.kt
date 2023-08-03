package com.smile.common.composables

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.smile.util.Constants
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText


@Composable
fun FloAppButton(@DrawableRes icon: Int, @StringRes contentDescription: Int, onClick: () -> Unit) {
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
fun BottomButton(@StringRes text: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
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

@Composable
fun NewContactButton(onClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Constants.HIGH_PADDING),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(Constants.VERY_HIGH_PADDING)
    ) {
        Icon(
            painter = painterResource(AppDrawable.baseline_person_add_alt_24),
            contentDescription = stringResource(AppText.new_contact),
            tint = MaterialTheme.colorScheme.surfaceTint
        )
        Text(text = stringResource(id = AppText.new_contact), fontWeight = FontWeight.SemiBold)
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonPreview() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AppFloActionButton({})
    }
}