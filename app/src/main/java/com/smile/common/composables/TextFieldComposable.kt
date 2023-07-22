package com.smile.common.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.smile.util.Constants.MEDIUM_PADDING
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText

@Composable
fun DefaultTextField(
    value: String,
    @StringRes placeholder: Int,
    onValueChange: (String) -> Unit
) {

    val shouldShowClearIcon = remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        placeholder = { Text(text = stringResource(id = placeholder)) },
        shape = RoundedCornerShape(MEDIUM_PADDING),
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
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
    @StringRes placeholder: Int,
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
        placeholder = { Text(text = stringResource(id = placeholder)) },
        shape = RoundedCornerShape(MEDIUM_PADDING),
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        trailingIcon = {
            IconButton(onClick = { isVisible = !isVisible }) {
                Icon(painter = painterResource(icon), contentDescription = null)
            }
        },
        onValueChange = onValueChange,
        visualTransformation = visualTransformation
    )
}


@Preview(showBackground = true)
@Composable
fun TextFieldPreview() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        DefaultTextField("", AppText.name) {}
    }
}