package net.limbuserendipity.customjuicecompose.ui.model

import androidx.compose.ui.graphics.Color

data class Ingredient(
    val name : String,
    val emoji : String,
    val color : Color,
    var fullness : Float = 0f
)