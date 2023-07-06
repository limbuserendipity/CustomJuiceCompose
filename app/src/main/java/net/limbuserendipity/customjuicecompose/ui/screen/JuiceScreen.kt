package net.limbuserendipity.customjuicecompose.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import net.limbuserendipity.customjuicecompose.R
import net.limbuserendipity.customjuicecompose.ui.component.IngredientPager
import net.limbuserendipity.customjuicecompose.ui.component.JuiceCup
import net.limbuserendipity.customjuicecompose.ui.model.Ingredient
import net.limbuserendipity.customjuicecompose.util.ingredientList
import net.limbuserendipity.customjuicecompose.util.round
import net.limbuserendipity.customjuicecompose.util.swap
import java.util.LinkedList
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

    val animateFullness = animateFloatAsState(targetValue = cupFullness)

    val isFull = cupFullness == 1f
    val juiceOz = 0.1f

    val pagerState: PagerState = rememberPagerState()
    val coroutine = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${(animateFullness.value * 100f).roundToInt()}%",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h2
                )
            }
        },
        bottomBar = {
            IngredientPager(
                ingredients = ingredients,
                onItemAddClick = { index, item ->
                    val fullness = if(item.fullness + juiceOz > 1f) 1f
                    else item.fullness + juiceOz
                    ingredients[index] = item.copy(fullness = fullness.round())
                },
                onItemRemoveClick = { index, item ->
                    val fullness = if(item.fullness - juiceOz < 0f) 0f
                    else item.fullness - juiceOz
                    ingredients[index] = item.copy(fullness = fullness.round())
                },
                isFull = isFull,
                pagerState = pagerState,
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
            onIngredientClick = { ingredient ->
                coroutine.launch {
                    pagerState.animateScrollToPage(ingredients.indexOf(ingredient))
                }
            },
            cupFullness = cupFullness,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )

    }

}
