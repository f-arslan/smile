package espressodev.smile.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import espressodev.smile.domain.util.Constants.HIGH_PADDING
import espressodev.smile.domain.util.Constants.MEDIUM_PADDING

@Composable
fun HeaderWrapper(content: @Composable () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MEDIUM_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}

@Composable
fun FormWrapper(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier.padding(
            start = HIGH_PADDING,
            top = MEDIUM_PADDING,
            end = HIGH_PADDING,
        ),
        verticalArrangement = Arrangement.spacedBy(HIGH_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}

