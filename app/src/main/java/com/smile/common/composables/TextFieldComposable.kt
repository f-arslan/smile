package com.smile.common.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.smile.util.Constants.MEDIUM_PADDING
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText

@Composable
fun DefaultTextField(
    value: String,
    @StringRes label: Int,
    onValueChange: (String) -> Unit
) {

    val shouldShowClearIcon = remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        label = { Text(text = stringResource(id = label)) },
        shape = RoundedCornerShape(MEDIUM_PADDING),
        trailingIcon = {
            if (shouldShowClearIcon.value) {
                IconButton(onClick = {
                    onValueChange("")
                    shouldShowClearIcon.value = false
                }) {
                    Icon(
                        painter = painterResource(AppDrawable.outline_cancel_24),
                        contentDescription = null
                    )
                }
            }
        },
        onValueChange = {
            onValueChange(it)
            shouldShowClearIcon.value = it.isNotEmpty()
        }
    )
}

@Composable
fun PasswordTextField(
    value: String,
    @StringRes label: Int,
    onValueChange: (String) -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    val icon =
        if (isVisible) AppDrawable.outline_visibility_24 else AppDrawable.outline_visibility_off_24

    val visualTransformation =
        if (isVisible) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        label = { Text(text = stringResource(id = label)) },
        shape = RoundedCornerShape(MEDIUM_PADDING),
        trailingIcon = {
            IconButton(onClick = { isVisible = !isVisible }) {
                Icon(painter = painterResource(icon), contentDescription = null)
            }
        },
        onValueChange = onValueChange,
        visualTransformation = visualTransformation
    )
}

@Composable
fun ContactTextField(textFieldValue: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
    var keyboardType by remember { mutableStateOf(KeyboardType.Text) }

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { onValueChange(it) },
        modifier = Modifier
            .fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        placeholder = {
            Text(text = stringResource(AppText.type_name_phone_number))
        },
        leadingIcon = {
            Text(
                text = stringResource(AppText.to),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        },
        trailingIcon = {
            when (keyboardType) {
                KeyboardType.Text -> {
                    IconButton(onClick = { keyboardType = KeyboardType.Number }) {
                        Icon(
                            painter = painterResource(AppDrawable.outline_keyboard_alt_24),
                            contentDescription = stringResource(id = AppText.keyboard),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                KeyboardType.Number -> {
                    IconButton(onClick = { keyboardType = KeyboardType.Text }) {
                        Icon(
                            painter = painterResource(AppDrawable.outline_dialpad_24),
                            contentDescription = stringResource(id = AppText.dial_pad),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}


@Preview(showBackground = true)
@Composable
fun TextFieldPreview() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

    }
}