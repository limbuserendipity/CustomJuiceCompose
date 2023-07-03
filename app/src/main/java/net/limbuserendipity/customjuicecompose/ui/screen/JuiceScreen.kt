package net.limbuserendipity.customjuicecompose.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import net.limbuserendipity.customjuicecompose.R
import net.limbuserendipity.customjuicecompose.ui.component.IngredientPager
import net.limbuserendipity.customjuicecompose.ui.component.JuiceCup
import net.limbuserendipity.customjuicecompose.util.ingredientList
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JuiceScreen(

) {

    val iconShape = ImageVector.vectorResource(id = R.drawable.cup_shape)
    val iconForeground = ImageVector.vectorResource(id = R.drawable.cup_foreground)

    val ingredients = remember { ingredientList.toMutableStateList() }
    val cupFullness: Float = ingredients.sumOf { it.fullness.toDouble() }.toFloat()
        .coerceIn(0f..1f)

    val isFull = cupFullness >= 1f
    val juiceOz = 0.10f

    Scaffold(
        topBar = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${(cupFullness * 100f).roundToInt()} %",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h2
                )
            }
        },
        bottomBar = {
            IngredientPager(
                ingredients = ingredients,
                onItemAddClick = { index, item ->
                    ingredients[index] =
                        item.copy(fullness = (item.fullness + juiceOz).coerceIn(0f..1f))
                },
                onItemRemoveClick = { index, item ->
                    ingredients[index] =
                        item.copy(fullness = (item.fullness - juiceOz).coerceIn(0f..1f))
                },
                isFull = isFull,
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
