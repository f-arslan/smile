package com.smile.common.ext

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smile.util.Constants.HIGH_PADDING
import com.smile.util.Constants.MEDIUM_PADDING
import com.smile.util.Constants.NO_PADDING

fun Modifier.textButton(): Modifier {
    return this.fillMaxWidth().padding(HIGH_PADDING, MEDIUM_PADDING, HIGH_PADDING, NO_PADDING)
}

fun Modifier.basicButton(): Modifier {
    return this.fillMaxWidth().padding(HIGH_PADDING, MEDIUM_PADDING)
}

fun Modifier.alertDialog(): Modifier {
    return this.wrapContentWidth().wrapContentHeight()
}