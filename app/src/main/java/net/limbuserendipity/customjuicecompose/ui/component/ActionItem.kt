package net.limbuserendipity.customjuicecompose.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ActionItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.caption,
    shape: Shape = CircleShape,
    color: Color = MaterialTheme.colors.primary,
    contentColor: Color = contentColorFor(color),
    paddingValues: PaddingValues = PaddingValues(4.dp),
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
) {
    Surface(
        onClick = onClick,
        shape = shape,
        color = color,
        contentColor = contentColor,
        border = border,
        elevation = elevation,
        modifier = modifier
            .defaultMinSize(28.dp)
            .animateContentSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(paddingValues)
        ) {
            Text(
                text = text,
                style = textStyle
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ActionItem(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    shape: Shape = CircleShape,
    color: Color = MaterialTheme.colors.primary,
    contentColor: Color = contentColorFor(color),
    paddingValues: PaddingValues = PaddingValues(4.dp),
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
) {
    Surface(
        onClick = onClick,
        shape = shape,
        color = color,
        contentColor = contentColor,
        border = border,
        elevation = elevation,
        modifier = modifier
            .defaultMinSize(28.dp)
            .animateContentSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(paddingValues)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription
            )
        }
    }
}

