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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import net.limbuserendipity.customjuicecompose.ui.model.ProgressState
import net.limbuserendipity.customjuicecompose.ui.model.UiState
import net.limbuserendipity.customjuicecompose.ui.theme.appleColor
import net.limbuserendipity.customjuicecompose.ui.theme.juiceGreen
import kotlin.math.roundToInt

@Composable
fun Progress(
    cupFullness: Float,
    uiState: UiState,
    onUiState: (UiState) -> Unit,
    modifier: Modifier = Modifier,
    progressState: ProgressState = uiState.progressState(),
    shape: Shape = CircleShape,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
) {

    if (cupFullness == 1f && uiState !is UiState.Completed)
        onUiState(UiState.Complete)

    val animateFullness = animateFloatAsState(
        targetValue = cupFullness,
        animationSpec = defaultProgressAnimationSpec(),
        finishedListener = {
            if (uiState == UiState.InProgress) onUiState(UiState.Quietly)
        }
    )

    val animateContentColor = animateColorAsState(targetValue = progressState.contentColor)
    val animateBackgroundColor = animateColorAsState(targetValue = progressState.backgroundColor)
    val animateContentScale = animateFloatAsState(targetValue = progressState.contentScale)

    Surface(
        shape = shape,
        color = animateBackgroundColor.value,
        contentColor = animateContentColor.value,
        border = border,
        elevation = elevation,
        modifier = Modifier
            .padding(16.dp)
            .scale(animateContentScale.value)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(8.dp)
        ) {

            AnimatedVisibility(
                visible = uiState is UiState.Completed
            ) {
                ActionItem(
                    icon = Icons.Default.ArrowBack,
                    onClick = {
                        onUiState(UiState.Complete)
                    },
                    color = MaterialTheme.colors.surface
                )
            }

            AnimatedVisibility(
                visible = uiState !is UiState.Completed
            ) {
                Text(
                    text = "${(animateFullness.value * 100f).roundToInt()}%",
                    fontWeight = FontWeight.Bold,
                    style = progressState.fullnessTextStyle
                )
            }

            AnimatedVisibility(
                visible = progressState.visibleCompleteContent
            ) {
                Text(
                    text = progressState.completeText,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h6
                )
            }

            AnimatedVisibility(
                visible = progressState.visibleCompleteContent
            ) {
                ActionItem(
                    icon = progressState.completeIcon,
                    onClick = {
                        onUiState(UiState.Completed)
                    },
                    color = progressState.completeIconBackground,
                    contentColor = progressState.completeIconColor
                )
            }

            AnimatedVisibility(
                visible = progressState.visibleCompletedContent,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                ActionItem(
                    icon = Icons.Default.Reorder,
                    onClick = {
                        onUiState(UiState.Completed)
                    },
                    color = MaterialTheme.colors.surface,
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

@Composable
fun UiState.progressState() = when (this) {
    is UiState.Quietly -> progressState()
    is UiState.InProgress -> progressState()
    is UiState.Complete -> progressState()
    is UiState.Completed -> progressState()
}

@Composable
fun UiState.Quietly.progressState() = ProgressState(
    backgroundColor = MaterialTheme.colors.surface,
    contentColor = Color.LightGray,
    contentScale = 0.8f,
    fullnessTextStyle = MaterialTheme.typography.h3,
    visibleCompleteContent = false,
    completeText = "",
    completeIcon = Icons.Default.KeyboardArrowRight,
    completeIconBackground = MaterialTheme.colors.primary,
    completeIconColor = MaterialTheme.colors.surface,
    visibleCompletedContent = false
)

@Composable
fun UiState.InProgress.progressState() = ProgressState(
    backgroundColor = MaterialTheme.colors.surface,
    contentColor = MaterialTheme.colors.primary,
    contentScale = 1f,
    fullnessTextStyle = MaterialTheme.typography.h3,
    visibleCompleteContent = false,
    completeText = "",
    completeIcon = Icons.Default.KeyboardArrowRight,
    completeIconBackground = MaterialTheme.colors.primary,
    completeIconColor = MaterialTheme.colors.surface,
    visibleCompletedContent = false
)

@Composable
fun UiState.Complete.progressState() = ProgressState(
    backgroundColor = contentColorFor(MaterialTheme.colors.surface),
    contentColor = contentColorFor(MaterialTheme.colors.primary),
    contentScale = 1f,
    fullnessTextStyle = MaterialTheme.typography.body2,
    visibleCompleteContent = true,
    completeText = "Complete",
    completeIcon = Icons.Default.ArrowForward,
    completeIconBackground = MaterialTheme.colors.surface,
    completeIconColor = MaterialTheme.colors.primary,
    visibleCompletedContent = false
)

@Composable
fun UiState.Completed.progressState() = ProgressState(
    backgroundColor = contentColorFor(MaterialTheme.colors.surface),
    contentColor = juiceGreen,
    contentScale = 1f,
    fullnessTextStyle = MaterialTheme.typography.h5,
    visibleCompleteContent = true,
    completeText = "Completed",
    completeIcon = Icons.Default.Check,
    completeIconBackground = MaterialTheme.colors.primary,
    completeIconColor = juiceGreen,
    visibleCompletedContent = true
)