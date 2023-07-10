package net.limbuserendipity.customjuicecompose.ui.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle

data class ProgressState(
    val backgroundColor : Color,
    val contentColor: Color,
    val contentScale : Float,
    val fullnessTextStyle: TextStyle,
    val completeText : String,
    val completeIcon : ImageVector,
    val visibleCompleteContent : Boolean
)