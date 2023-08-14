package com.smile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smile.common.composables.AppFloActionButton
import com.smile.common.composables.AppSearchBar
import com.smile.common.composables.CountCircle
import com.smile.common.composables.LetterInCircle
import com.smile.model.room.ContactEntity
import com.smile.model.service.module.Response
import com.smile.ui.view_models.HomeScreenViewModel
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.Constants.MAX_PADDING
import com.smile.util.Constants.MEDIUM_HIGH_PADDING

@Composable
fun HomeScreenProvider(navigate: () -> Unit, viewModel: HomeScreenViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) { viewModel.getContacts() }
    val contacts by viewModel.contacts.collectAsStateWithLifecycle()
    Scaffold(
        floatingActionButton = { AppFloActionButton(onClick = navigate) },
        topBar = {
            AppSearchBar(
                userLetter = "A", "A", false,
                {}, {}, {}, {}
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            when (val res = contacts) {
                is Response.Success -> {
                    LastContactList(res.data)
                }

                Response.Loading -> {} // TODO: Can be added loading state placeholder
                else -> {}
            }
        }
    }
}

@Composable
fun LastContactList(data: List<List<ContactEntity>>) {
}

@Composable
fun LastContactItem(isNotSeen: Boolean) {
    val fontWeight = if (isNotSeen) {
        FontWeight.Bold
    } else {
        FontWeight.Normal
    }
    Row(modifier = Modifier.padding(MEDIUM_HIGH_PADDING)) {
        LetterInCircle(letter = "A")
        Spacer(Modifier.width(HIGH_PADDING))
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Title",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = fontWeight
                )
                Spacer(Modifier.width(MAX_PADDING))
                Text(text = "Aug 1", fontWeight = fontWeight)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Super uber hello again how are you are you miss me did you hear me what I said?",
                    maxLines = 2,
                    modifier = Modifier.weight(1f),
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = fontWeight
                )
                Spacer(Modifier.width(MAX_PADDING))
                CountCircle(letter = "2", fontWeight)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LastContactPreview() {
    LastContactItem(true)
}


