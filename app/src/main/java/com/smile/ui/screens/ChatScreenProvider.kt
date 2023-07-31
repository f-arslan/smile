package com.smile.ui.screens

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.smile.common.composables.ChatField
import com.smile.ui.view_models.ChatScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun ChatScreenProvider(viewModel: ChatScreenViewModel = hiltViewModel()) {
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    val scrollState = rememberLazyListState()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {

        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        Messages(modifier = Modifier.padding(paddingValues))
        ChatField(
            onMessageSent = {

            },
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

@Composable
fun Messages(modifier: Modifier = Modifier) {
    TODO("Not yet implemented")
}


@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {

}