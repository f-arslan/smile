package espressodev.smile.ui.screens.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import espressodev.smile.common.composables.ChatField
import espressodev.smile.common.composables.ContactTopAppBar
import espressodev.smile.common.composables.FunctionalityNotAvailablePopup
import espressodev.smile.data.service.model.Contact
import espressodev.smile.data.service.model.Message
import espressodev.smile.data.service.model.Response
import espressodev.smile.domain.util.Constants.HIGH_PADDING
import espressodev.smile.domain.util.Constants.HIGH_PLUS_PADDING
import espressodev.smile.domain.util.Constants.MAX_PADDING
import espressodev.smile.domain.util.Constants.MEDIUM_HIGH_PADDING
import espressodev.smile.domain.util.Constants.MEDIUM_PADDING
import espressodev.smile.domain.util.Constants.SMALL_PADDING
import espressodev.smile.domain.util.Constants.VERY_SMALL_PADDING
import espressodev.smile.domain.util.isTodayOrDate
import espressodev.smile.domain.util.timestampToDate

@Composable
fun ChatRoute(
    contactId: String,
    roomId: String,
    popUp: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) { viewModel.getContactAndMessage(contactId, roomId) }
    val contactState by viewModel.contactState.collectAsStateWithLifecycle()
    val messages by viewModel.messagesState.collectAsStateWithLifecycle()
    if (contactState is Response.Success && messages is Response.Success) {
        ChatScreen(
            contact = (contactState as Response.Success<Contact>).data,
            popUp = popUp,
            onMessageSent = { message ->
                viewModel.sendMessage(message, roomId, contactId)
            },
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
                .padding(paddingValues),
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

@Composable
fun DayHeader(
    dayString: String,
    style: TextStyle = MaterialTheme.typography.labelSmall,
    height: Dp = HIGH_PADDING
) {
    Row(
        modifier = Modifier
            .padding(vertical = MEDIUM_PADDING, horizontal = HIGH_PADDING)
            .height(height)
    ) {
        DayHeaderLine()
        Text(
            text = dayString,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = style,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        DayHeaderLine()
    }
}

@Composable
private fun RowScope.DayHeaderLine() {
    Divider(
        modifier = Modifier
            .weight(1f)
            .align(Alignment.CenterVertically),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
}

private val ChatBubbleShape = RoundedCornerShape(HIGH_PLUS_PADDING)

@Composable
fun ChatItemBubble(message: Message, isUserMe: Boolean) {
    val backgroundBubbleColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val paddingModifier =
        if (isUserMe) Modifier.padding(start = MAX_PADDING) else Modifier.padding(end = MAX_PADDING)
    val interactionSource = remember { MutableInteractionSource() }
    val isTimestampVisible = remember { mutableStateOf(false) }
    Row(
        horizontalArrangement = if (isUserMe) Arrangement.End else Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .then(paddingModifier)
    ) {
        Column(horizontalAlignment = Alignment.End) {
            Surface(
                color = backgroundBubbleColor, shape = ChatBubbleShape,
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    isTimestampVisible.value = !isTimestampVisible.value
                }
            ) {
                Text(
                    text = message.content,
                    modifier = Modifier.padding(MEDIUM_HIGH_PADDING)
                )
            }
            Spacer(Modifier.padding(VERY_SMALL_PADDING))
            AnimatedVisibility(visible = isTimestampVisible.value) {
                Text(
                    text = timestampToDate(message.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = SMALL_PADDING, end = SMALL_PADDING)
                )
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
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
            val messageGrouped = messages.groupBy { isTodayOrDate(it.timestamp) }
            messageGrouped.forEach { (date, messagesForDate) ->
                items(messagesForDate, key = { it.messageId }) {
                    ChatItemBubble(message = it, isUserMe = it.senderId == currentUserId)
                }
                stickyHeader {
                    DayHeader(dayString = date)
                }
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
   Messages(messages = listOf(), currentUserId = "massa", scrollState = rememberLazyListState(), modifier = Modifier)
}