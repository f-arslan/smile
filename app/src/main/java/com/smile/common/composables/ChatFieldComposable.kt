package com.smile.common.composables

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smile.util.Constants.SMALL_PADDING
import com.smile.util.Constants.VERY_HIGH_PADDING
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText

enum class InputSelector { NONE, EMOJI, ATTACH }

@Composable
fun ChatField(
    onMessageSent: (String) -> Unit,
    modifier: Modifier = Modifier,
    resetScroll: () -> Unit = {}
) {
    var currentInputSelector by rememberSaveable { mutableStateOf(InputSelector.NONE) }
    val dismissKeyboard = { currentInputSelector = InputSelector.NONE }

    if (currentInputSelector != InputSelector.NONE) {
        BackHandler(onBack = dismissKeyboard)
    }

    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    // Used to decide if the keyboard should be shown
    var textFieldFocusState by remember { mutableStateOf(false) }

    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(SMALL_PADDING)) {
        UserInputText(
            textFieldValue = textState,
            onTextChanged = { textState = it },
            keyboardShown = currentInputSelector == InputSelector.NONE && textFieldFocusState,
            onTextFieldFocused = { focused ->
                if (focused) {
                    currentInputSelector = InputSelector.NONE
                    resetScroll()
                }
                textFieldFocusState = focused
            },
            focusState = textFieldFocusState
        )
        ChatButton()
    }
}

@Composable
fun UserInputText(
    keyboardType: KeyboardType = KeyboardType.Text,
    textFieldValue: TextFieldValue,
    onTextChanged: (TextFieldValue) -> Unit,
    keyboardShown: Boolean,
    onTextFieldFocused: (Boolean) -> Unit,
    focusState: Boolean
) {
    var lastFocusState by remember { mutableStateOf(false) }
    val contentDesc = stringResource(id = AppText.chat_text_field_desc)
    OutlinedTextField(
        value = textFieldValue,
        leadingIcon = {
            DefaultIconButton(AppDrawable.baseline_mood_24, AppText.smile_icon) {
                TODO("Handle Open emojis")
            }
        },
        trailingIcon = {
            Row {
                DefaultIconButton(AppDrawable.baseline_attach_file_24, AppText.attach_icon) {
                    TODO("Handle Attach item case")
                }
            }
        },
        placeholder = {
            if (textFieldValue.text.isEmpty() && !focusState) {
                Text(text = stringResource(id = AppText.chat_text_field_placeholder))
            }
        },
        shape = RoundedCornerShape(VERY_HIGH_PADDING),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        onValueChange = { onTextChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { state ->
                if (lastFocusState != state.isFocused) {
                    onTextFieldFocused(state.isFocused)
                }
                lastFocusState = state.isFocused
            }
            .semantics {
                contentDescription = contentDesc
                keyboardShownProperty = keyboardShown
            },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        maxLines = 1,
    )
}

@Composable
fun ChatButton() {
    FilledTonalIconButton(onClick = {}, modifier = Modifier.size(54.dp)) {
        Icon(
            imageVector = Icons.Outlined.Send,
            contentDescription = stringResource(AppText.send_icon),
        )
    }
}

@Composable
fun DefaultIconButton(@DrawableRes icon: Int, @StringRes desc: Int, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = stringResource(id = desc)
        )
    }
}

val KeyboardShownKey = SemanticsPropertyKey<Boolean>("KeyboardShownKey")
var SemanticsPropertyReceiver.keyboardShownProperty by KeyboardShownKey

@Composable
@Preview(showBackground = true)
fun ChatFieldPreview() {
    UserInputText(
        keyboardType = KeyboardType.Text,
        onTextChanged = {},
        textFieldValue = TextFieldValue(""),
        keyboardShown = false,
        onTextFieldFocused = {},
        focusState = false
    )
}