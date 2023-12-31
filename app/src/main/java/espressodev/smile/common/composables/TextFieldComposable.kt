package espressodev.smile.common.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import espressodev.smile.domain.util.Constants.MEDIUM_PADDING
import espressodev.smile.R.drawable as AppDrawable
import espressodev.smile.R.string as AppText

@Composable
fun DefaultTextField(
    value: String,
    @StringRes label: Int,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
) {
    val showClearIcon by remember(value) { derivedStateOf { value.isNotEmpty() } }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        enabled = enabled,
        label = { Text(text = stringResource(id = label)) },
        shape = RoundedCornerShape(MEDIUM_PADDING),
        trailingIcon = {
            if (showClearIcon) {
                IconButton(onClick = {
                    onValueChange("")
                }) {
                    Icon(
                        painter = painterResource(AppDrawable.outline_cancel_24),
                        contentDescription = null
                    )
                }
            }
        },
        onValueChange = onValueChange
    )
}

@Composable
fun PasswordTextField(
    value: String,
    @StringRes label: Int,
    onValueChange: (String) -> Unit,
    onConfirmClick: () -> Unit = {}
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
                Icon(painter = painterResource(icon), contentDescription = stringResource(label))
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onNext = { onConfirmClick() }
        ),
        onValueChange = onValueChange,
        visualTransformation = visualTransformation
    )
}

@Composable
fun ContactTextField(
    query: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        placeholder = { Text(text = stringResource(AppText.type_name)) },
        leadingIcon = {
            Icon(painterResource(AppDrawable.person_search_24), stringResource(AppText.search))
        },
        maxLines = 1,
    )
}


@Preview(showBackground = true)
@Composable
fun TextFieldPreview() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        ContactTextField(query = "HELLO AGAIN", onValueChange = {})
    }
}