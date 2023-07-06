package net.limbuserendipity.customjuicecompose.ui.model

import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.Color

data class Ingredient(
    val name : String,
    val emoji : String,
    val color : Color,
    @FloatRange(0.0,1.0)
    var fullness : Float = 0f
)