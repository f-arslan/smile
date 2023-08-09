package com.smile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.ExperimentalComposeUiApi
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
import com.smile.model.MessageStatus
import com.smile.model.service.module.Response
import com.smile.ui.view_models.ChatScreenViewModel
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.Constants.HIGH_PLUS_PADDING
import com.smile.util.Constants.MEDIUM_HIGH_PADDING
import com.smile.util.Constants.MEDIUM_PADDING
import com.smile.util.Constants.SMALL_PADDING
import com.smile.util.Constants.VERY_HIGH_PADDING
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun ChatScreenProvider(
    contactId: String,
    popUp: () -> Unit,
    viewModel: ChatScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getContactAndMessage(contactId)
    }
    val contactState by viewModel.contactState.collectAsStateWithLifecycle()
    val messages by viewModel.messagesState.collectAsStateWithLifecycle()
    if (contactState is Response.Success && messages is Response.Success) {
        ChatScreen(
            contact = (contactState as Response.Success<Contact>).data,
            popUp = popUp,
            onMessageSent = { viewModel.sendMessage(it, contactId) },
            messages = (messages as Response.Success<List<Message>>).data,
            currentUserId = viewModel.currentUserId
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    contact: Contact,
    popUp: () -> Unit,
    onMessageSent: (String) -> Unit,
    messages: List<Message>,
    currentUserId: String
) {
    var notFunctionalState by remember { mutableStateOf(false) }
    if (notFunctionalState) {
        FunctionalityNotAvailablePopup { notFunctionalState = false }
    }

    val scrollState = rememberLazyListState()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)


    Scaffold(
        topBar = {
            ContactTopAppBar(
                contactName = contact.firstName + " " + contact.lastName,
                popUp = popUp,
                onMoreClick = { notFunctionalState = true })
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Messages(
                messages = messages,
                currentUserId,
                scrollState,
                modifier = Modifier.weight(1f),
            )
            ChatField(
                onMessageSent = {
                    onMessageSent(it)
                },
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
            )
        }
    }
}

private val ChatBubbleShape =
    RoundedCornerShape(HIGH_PLUS_PADDING)

@Composable
fun ChatItemBubble(message: Message, isUserMe: Boolean) {
    val backgroundBubbleColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Surface(
        color = backgroundBubbleColor, shape = ChatBubbleShape
    ) {
        Text(text = message.content, modifier = Modifier.padding(MEDIUM_HIGH_PADDING))
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Messages(
    messages: List<Message>,
    currentUserId: String,
    scrollState: LazyListState,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        LazyColumn(
            reverseLayout = true,
            state = scrollState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(HIGH_PADDING),
            verticalArrangement = Arrangement.spacedBy(
                MEDIUM_PADDING
            )
        ) {
            items(messages, key = { it.messageId }) { // It can be error maybe messageId empty
                ChatItemBubble(message = it, isUserMe = it.senderId == currentUserId)
            }
        }
        LaunchedEffect(messages.size) {
            scrollState.animateScrollToItem(0)
        }
    }

}


@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    ChatItemBubble(
        message = Message(
            messageId = "efficiantur",
            senderId = "ubique",
            recipientId = "gravida",
            content = "faucibus",
            timestamp = 4199,
            readBy = listOf(),
            status = MessageStatus.SENT
        ), isUserMe = false

    )
}