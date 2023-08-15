package com.smile.common.composables

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
import androidx.compose.ui.text.font.FontWeight
import com.smile.util.Constants
import com.smile.util.Constants.COUNT_BUBBLE_SIZE
import com.smile.util.Constants.VERY_SMALL_PADDING

val colorStops = arrayOf(
    0.0f to Color.Yellow,
    0.2f to Color.Red,
    1f to Color.Blue
)

const val borderWidth = 16f

@Composable
fun UserAvatar(letter: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "Infinity Transition")
    val rotationAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing)),
        label = "Rotation Animation"
    )
    Surface(
        modifier = Modifier
            .size(Constants.AVATAR_SIZE)
            .drawBehind {
                rotate(rotationAnimation.value) {
                    drawCircle(
                        Brush.horizontalGradient(colorStops = colorStops),
                        style = Stroke(borderWidth)
                    )
                }
            }
            .padding(VERY_SMALL_PADDING)
            .clip(CircleShape),
        color = MaterialTheme.colorScheme.primary
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
fun LetterInCircle(letter: String) {
    Surface(
        modifier = Modifier
            .size(Constants.AVATAR_SIZE)
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