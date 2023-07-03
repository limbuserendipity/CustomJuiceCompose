package net.limbuserendipity.customjuicecompose.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import net.limbuserendipity.customjuicecompose.R
import net.limbuserendipity.customjuicecompose.ui.component.IngredientPager
import net.limbuserendipity.customjuicecompose.ui.component.JuiceCup
import net.limbuserendipity.customjuicecompose.util.ingredientList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JuiceScreen(

) {

    val iconShape = ImageVector.vectorResource(id = R.drawable.cup_shape)
    val iconForeground = ImageVector.vectorResource(id = R.drawable.cup_foreground)

    val ingredients = remember { ingredientList.toMutableStateList() }
    val cupFullness: Float = ingredients.sumOf { it.fullness.toDouble() }.toFloat()

    Scaffold(
        bottomBar = {
            IngredientPager(
                ingredients = ingredients,
                onItemClick = { index, item ->
                    ingredients[index] = item.copy(fullness = item.fullness + 0.10f)
                },
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp)
                    .fillMaxWidth()
            )
        },
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
