package net.limbuserendipity.customjuicecompose.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.animate
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import net.limbuserendipity.customjuicecompose.R
import net.limbuserendipity.customjuicecompose.ui.component.IngredientPager
import net.limbuserendipity.customjuicecompose.ui.component.JuiceCup
import net.limbuserendipity.customjuicecompose.ui.component.Receipt
import net.limbuserendipity.customjuicecompose.ui.component.Progress
import net.limbuserendipity.customjuicecompose.ui.model.UiState
import net.limbuserendipity.customjuicecompose.util.ingredientList
import net.limbuserendipity.customjuicecompose.util.round

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun JuiceScreen(

) {

    var uiState: UiState by remember { mutableStateOf(UiState.Quietly) }

    val juiceOz = 0.1f

    val ingredients = remember { ingredientList.toMutableStateList() }
    val cupFullness: Float = ingredients.sumOf { it.fullness.toDouble() }.toFloat()
        .coerceIn(0f..1f)

    if (cupFullness == 1f && uiState !is UiState.Completed) uiState = UiState.Complete

    val iconShape = ImageVector.vectorResource(id = R.drawable.cup_shape)
    val iconForeground = ImageVector.vectorResource(id = R.drawable.cup_foreground)

    val pagerState: PagerState = rememberPagerState()
    val coroutine = rememberCoroutineScope()

    var degrees by remember { mutableStateOf(0f) }

    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(
        sheetContent = {
            Receipt(
                ingredients = ingredients.filter { it.fullness > 0f },
                modifier = Modifier.fillMaxWidth()
            )
        },
        sheetState = modalBottomSheetState,
        sheetShape = RectangleShape,
        sheetBackgroundColor = MaterialTheme.colors.primary
    ) {
        Scaffold(
            topBar = {
                Progress(
                    cupFullness = cupFullness,
                    uiState = uiState,
                    onUiState = { state ->
                        uiState = state
                    },
                    onReceiptActionClick = {
                        coroutine.launch {
                            modalBottomSheetState.show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            bottomBar = {
                AnimatedVisibility(
                    visible = uiState !is UiState.Completed,
                    enter = slideInVertically { height -> height } + fadeIn(),
                    exit = slideOutVertically { height -> height } + fadeOut()
                ) {
                    IngredientPager(
                        ingredients = ingredients,
                        onItemAddClick = { index, item ->
                            val fullness = if (item.fullness + juiceOz > 1f) 1f
                            else item.fullness + juiceOz
                            ingredients[index] = item.copy(fullness = fullness.round())
                            uiState = UiState.InProgress
                        },
                        onItemRemoveClick = { index, item ->
                            val fullness = if (item.fullness - juiceOz < 0f) 0f
                            else item.fullness - juiceOz
                            ingredients[index] = item.copy(fullness = fullness.round())
                            uiState = UiState.InProgress
                        },
                        uiState = uiState,
                        pagerState = pagerState,
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 16.dp)
                            .fillMaxWidth()
                    )
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize()
        ) { paddingValues ->

            JuiceCup(
                imageShape = iconShape,
                imageForeground = iconForeground,
                ingredients = ingredients
                    .filter { it.fullness > 0f },
                onIngredientClick = { ingredient ->
                    coroutine.launch {
                        pagerState.animateScrollToPage(ingredients.indexOf(ingredient))
                    }
                },
                cupFullness = cupFullness,
                isCompleted = uiState is UiState.Completed,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .rotate(degrees)
            )

        }
    }

    LaunchedEffect(key1 = uiState is UiState.Completed) {
        if (uiState is UiState.Completed) {
            repeat(2) {
                animate(degrees, 4f) { value, _ ->
                    degrees = value
                }
                animate(degrees, -4f) { value, _ ->
                    degrees = value
                }
            }
        }
        animate(degrees, 0f) { value, _ ->
            degrees = value
        }
    }

}
