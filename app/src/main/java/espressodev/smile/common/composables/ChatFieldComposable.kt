package espressodev.smile.common.composables

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import espressodev.smile.domain.util.Constants.MEDIUM_PADDING
import espressodev.smile.domain.util.Constants.NO_PADDING
import espressodev.smile.domain.util.Constants.SMALL_PADDING
import espressodev.smile.domain.util.Constants.VERY_HIGH_PADDING
import espressodev.smile.R.drawable as AppDrawable
import espressodev.smile.R.string as AppText



@Composable
fun ChatField(
    onMessageSent: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    val isTextFieldBlank = remember { derivedStateOf { textState.text.isBlank() } }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(MEDIUM_PADDING),
    ) {
        UserInputTextField(
            modifier = Modifier.weight(1f),
            textFieldValue = textState,
            onTextChanged = { textState = it },
        )
        Spacer(modifier = Modifier.padding(SMALL_PADDING))
        ChatButton(enabled = !isTextFieldBlank.value) {
            onMessageSent(textState.text)
            textState = TextFieldValue()
        }
    }
}

@Composable
fun UserInputTextField(
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    textFieldValue: TextFieldValue,
    onTextChanged: (TextFieldValue) -> Unit,
) {
    var dialogState by rememberSaveable { mutableStateOf(false) }
    if (dialogState) {
        FunctionalityNotAvailablePopup { dialogState = false }
    }
    OutlinedTextField(
        value = textFieldValue,
        trailingIcon = {
            Row {
                DefaultIconButton(AppDrawable.baseline_attach_file_24, AppText.attach_icon) {
                    dialogState = true
                }
            }
        },
        placeholder = {
            if (textFieldValue.text.isEmpty()) {
                Text(text = stringResource(id = AppText.chat_text_field_placeholder))
            }
        },
        shape = RoundedCornerShape(VERY_HIGH_PADDING),
        onValueChange = { onTextChanged(it) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier,
    )
}

@Composable
fun ChatButton(enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(54.dp)
            .clip(CircleShape),
        contentPadding = PaddingValues(NO_PADDING),
        enabled = enabled
    ) {
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


@Composable
@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
fun ChatFieldPreview() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        UserInputTextField(
            keyboardType = KeyboardType.Text,
            textFieldValue = TextFieldValue(""),
            onTextChanged = {},
        )
    }
}