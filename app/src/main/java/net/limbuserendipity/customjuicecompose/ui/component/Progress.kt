package net.limbuserendipity.customjuicecompose.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import net.limbuserendipity.customjuicecompose.ui.model.ProgressState
import net.limbuserendipity.customjuicecompose.ui.theme.appleColor
import net.limbuserendipity.customjuicecompose.ui.theme.coconutColor
import kotlin.math.roundToInt

@Composable
fun Progress(
    cupFullness: Float,
    progressState: ProgressState,
    onProgressState: (ProgressState) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    defaultColor: Color = MaterialTheme.colors.surface,
    defaultContentColor: Color = MaterialTheme.colors.primary,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
) {

    if (cupFullness == 1f && progressState != ProgressState.Completed)
        onProgressState(ProgressState.Complete)

    val animateFullness = animateFloatAsState(
        targetValue = cupFullness,
        animationSpec = defaultProgressAnimationSpec(),
        finishedListener = {
            if (progressState == ProgressState.Progress) onProgressState(ProgressState.Quietly)
        }
    )
    val backgroundColor : Color
    val contentColor: Color
    val contentScale : Float
    val fullnessTextStyle: TextStyle
    val completeText : String
    val completeIcon : ImageVector
    val visibleCompleteContent : Boolean
    when (progressState) {
        ProgressState.Quietly -> {
            backgroundColor = defaultColor
            contentColor = Color.LightGray
            contentScale = 0.8f
            fullnessTextStyle = MaterialTheme.typography.h3
            completeText = ""
            completeIcon = Icons.Default.KeyboardArrowRight
            visibleCompleteContent = false
        }
        ProgressState.Progress -> {
            backgroundColor = defaultColor
            contentColor = defaultContentColor
            contentScale = 1f
            fullnessTextStyle = MaterialTheme.typography.h3
            completeText = ""
            completeIcon = Icons.Default.KeyboardArrowRight
            visibleCompleteContent = false
        }
        ProgressState.Complete -> {
            backgroundColor = contentColorFor(defaultColor)
            contentColor = contentColorFor(defaultContentColor)
            contentScale = 1f
            fullnessTextStyle = MaterialTheme.typography.body2
            completeText = "Complete"
            completeIcon = Icons.Default.KeyboardArrowRight
            visibleCompleteContent = true
        }
        ProgressState.Completed -> {
            backgroundColor = contentColorFor(defaultColor)
            contentColor = appleColor
            contentScale = 1f
            fullnessTextStyle = MaterialTheme.typography.h5
            completeText = "Completed"
            completeIcon = Icons.Default.Check
            visibleCompleteContent = true
        }
    }

    val animateContentColor = animateColorAsState(targetValue = contentColor)
    val animateBackgroundColor = animateColorAsState(targetValue = backgroundColor)
    val animateContentScale = animateFloatAsState(targetValue = contentScale)

    Surface(
        shape = shape,
        color = animateBackgroundColor.value,
        contentColor = animateContentColor.value,
        border = border,
        elevation = elevation,
        modifier = Modifier
            .padding(8.dp)
            .scale(animateContentScale.value)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(8.dp)
        ) {

            AnimatedVisibility(
                visible = progressState == ProgressState.Completed
            ) {
                ActionItem(
                    icon = Icons.Default.ArrowBack,
                    onClick = {
                        onProgressState(ProgressState.Complete)
                    },
                    color = MaterialTheme.colors.surface
                )
            }

            AnimatedVisibility(
                visible = progressState != ProgressState.Completed
            ) {
                Text(
                    text = "${(animateFullness.value * 100f).roundToInt()}%",
                    fontWeight = FontWeight.Bold,
                    style = fullnessTextStyle
                )
            }

            AnimatedVisibility(
                visible = visibleCompleteContent
            ) {
                Text(
                    text = completeText,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h5
                )
            }

            AnimatedVisibility(
                visible = visibleCompleteContent
            ) {
                ActionItem(
                    icon = completeIcon,
                    onClick = {
                        onProgressState(ProgressState.Completed)
                    },
                    color = MaterialTheme.colors.surface
                )
            }
        }
    }

}

fun <T> defaultProgressAnimationSpec(
    durationMillis: Int = 300,
    delayMillis: Int = 0,
    easing: Easing = FastOutSlowInEasing
) = tween<T>(
    durationMillis = durationMillis,
    delayMillis = delayMillis,
    easing = easing
)