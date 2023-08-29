package com.smile.common.composables

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.smile.util.Constants
import com.smile.util.Constants.AVATAR_SIZE
import com.smile.util.Constants.COUNT_BUBBLE_SIZE
import com.smile.util.Constants.GENERAL_ICON_SIZE
import com.smile.util.Constants.ICON_SIZE
import com.smile.util.Constants.SMALL_MEDIUM_PADDING
import com.smile.util.Constants.SMALL_PADDING
import com.smile.util.Constants.VERY_SMALL_PADDING
import com.smile.R.drawable as AppDrawable
import com.smile.R.string as AppText

@Composable
fun colorStops(): Array<out Pair<Float, Color>> {
    return arrayOf(
        0.0f to MaterialTheme.colorScheme.primaryContainer,
        0.4f to MaterialTheme.colorScheme.primary,
        1f to MaterialTheme.colorScheme.primaryContainer
    )
}

const val borderWidth = 16f

@Composable
fun UserAvatar(letter: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "Infinity Transition")
    val rotationAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing)),
        label = "Rotation Animation"
    )
    val colors = colorStops()
    Surface(
        modifier = Modifier
            .size(Constants.AVATAR_SIZE)
            .drawBehind {
                rotate(rotationAnimation.value) {
                    drawCircle(
                        Brush.horizontalGradient(colorStops = colors),
                        style = Stroke(borderWidth)
                    )
                }
            }
            .padding(VERY_SMALL_PADDING)
            .clip(CircleShape),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = letter.uppercase(), style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
fun LetterInCircle(letter: String, size: Dp) {
    Surface(
        modifier = Modifier
            .size(size)
            .clip(CircleShape),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = letter.uppercase(), style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
fun LetterInCircle(letter: Char) {
    val infiniteTransition = rememberInfiniteTransition(label = "Infinity Transition")
    val rotationAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing)),
        label = "Rotation Animation"
    )
    val colors = colorStops()
    Surface(
        modifier = Modifier
            .size(ICON_SIZE)
            .drawBehind {
                rotate(rotationAnimation.value) {
                    drawCircle(
                        Brush.horizontalGradient(colorStops = colors),
                        style = Stroke(borderWidth)
                    )
                }
            }
            .clip(CircleShape),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = letter.uppercase(), style = MaterialTheme.typography.displayLarge)
        }
    }
}

@Composable
fun IconCircle(@DrawableRes icon: Int, @StringRes iconDesc: Int) {
    Surface(
        modifier = Modifier
            .size(AVATAR_SIZE)
            .clip(CircleShape),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = stringResource(iconDesc),
            modifier = Modifier.padding(SMALL_MEDIUM_PADDING)
        )
    }
}

@Composable
fun CountCircle(letter: String) {
    Surface(
        modifier = Modifier
            .size(COUNT_BUBBLE_SIZE)
            .clip(CircleShape),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = letter,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun IconPreview() {
    IconCircle(icon = AppDrawable.round_logout_24, iconDesc = AppText.logout)
}