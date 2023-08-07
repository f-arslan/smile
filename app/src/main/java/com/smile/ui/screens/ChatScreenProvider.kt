package com.smile.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smile.common.composables.ChatField
import com.smile.common.composables.ContactTopAppBar
import com.smile.common.composables.FunctionalityNotAvailablePopup
import com.smile.model.Contact
import com.smile.model.Message
import com.smile.model.service.module.Response
import com.smile.ui.view_models.ChatScreenViewModel
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.Constants.SMALL_PADDING
import kotlinx.coroutines.launch

@Composable
fun ChatScreenProvider(
    contactId: String,
    popUp: () -> Unit,
    viewModel: ChatScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getContact(contactId)
        viewModel.getMessage(contactId)
    }
    val contactState by viewModel.contactState.collectAsStateWithLifecycle()
    val messages by viewModel.messagesState.collectAsStateWithLifecycle()
    Log.d("ChatScreenProvider", "messages: $messages")
    when (val res = contactState) {
        is Response.Success -> {
            ChatScreen(res.data, popUp) {
                viewModel.sendMessage(it, res.data.contactUserId)
            }
        }

        else -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(contact: Contact, popUp: () -> Unit, onMessageSent: (String) -> Unit) {
    val scrollState = rememberLazyListState()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)
    val scope = rememberCoroutineScope()
    var notFunctionalState by remember { mutableStateOf(false) }
    if (notFunctionalState) {
        FunctionalityNotAvailablePopup {
            notFunctionalState = false
        }
    }
    Scaffold(
        topBar = {
            ContactTopAppBar(
                contactName = contact.firstName + " " + contact.lastName,
                popUp = popUp,
                onMoreClick = {
                    notFunctionalState = true
                })
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Messages(modifier = Modifier.weight(1f))
            Surface {
                ChatField(
                    onMessageSent = onMessageSent,
                    resetScroll = {
                        scope.launch {
                            scrollState.scrollToItem(0)
                        }
                    },
                    modifier = Modifier
                        .navigationBarsPadding()
                        .imePadding()
                )
            }
        }
    }
}

private val ChatBubbleShape =
    RoundedCornerShape(SMALL_PADDING, HIGH_PADDING, HIGH_PADDING, HIGH_PADDING)

@Composable
fun ChatItemBubble(message: Message, isUserMe: Boolean) {
    val backgroundBubbleColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Surface(color = backgroundBubbleColor, shape = ChatBubbleShape) {
        Text(text = message.content)
    }
}

@Composable
fun Messages(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = "Hello")
    }
}


@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    ChatItemBubble(
        message = Message(
            messageId = "vivamus",
            senderId = "vim",
            recipientId = "ridiculus",
            content = "feugiat",
            timestamp = 7807,
            readBy = listOf(),
        ), isUserMe = false
    )
}