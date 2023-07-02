package net.limbuserendipity.customjuicecompose.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import net.limbuserendipity.customjuicecompose.R
import net.limbuserendipity.customjuicecompose.ui.component.JuiceCup
import net.limbuserendipity.customjuicecompose.util.ingredientList

@Composable
fun JuiceScreen(

) {

    val iconShape = ImageVector.vectorResource(id = R.drawable.cup_shape)
    val iconForeground = ImageVector.vectorResource(id = R.drawable.cup_foreground)

    val ingredients = remember { ingredientList.toMutableStateList() }
    val cupFullness: Float = ingredients.sumOf { it.fullness.toDouble() }.toFloat()

    ingredients.map { it.fullness = 0.1f }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        JuiceCup(
            imageShape = iconShape,
            imageForeground = iconForeground,
            ingredients = ingredients,
            cupFullness = cupFullness,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )

    }

}
