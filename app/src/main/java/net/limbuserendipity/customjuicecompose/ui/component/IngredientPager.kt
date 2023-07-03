package net.limbuserendipity.customjuicecompose.ui.component

import android.util.DebugUtils
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import net.limbuserendipity.customjuicecompose.ui.model.Ingredient
import net.limbuserendipity.customjuicecompose.util.coloredShadow
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IngredientPager(
    ingredients: List<Ingredient>,
    onItemAddClick: (Int, Ingredient) -> Unit,
    onItemRemoveClick: (Int, Ingredient) -> Unit,
    isFull: Boolean,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(),
    pageSize: Dp = 104.dp
) {

    val pageModifier = Modifier

    HorizontalPager(
        pageCount = ingredients.size,
        state = pagerState,
        contentPadding = PaddingValues(start = pageSize, end = pageSize),
        modifier = modifier
    ) { index ->
        val pageOffset = ((pagerState.currentPage - index) +
                pagerState.currentPageOffsetFraction).absoluteValue
        val item = ingredients[index]
        IngredientCard(
            isSelected = pagerState.currentPage == index,
            ingredient = item,
            onAddClick = { onItemAddClick(index, item) },
            onRemoveClick = { onItemRemoveClick(index, item) },
            size = pageSize,
            isFull = isFull,
            pageOffset = pageOffset,
            modifier = pageModifier
        )
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun IngredientCard(
    isSelected : Boolean,
    ingredient: Ingredient,
    onAddClick: () -> Unit,
    onRemoveClick: () -> Unit,
    size: Dp,
    isFull: Boolean,
    pageOffset : Float,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(4.dp),
    shape: Shape = CircleShape,
    color: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(color),
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
) {

    val alpha = lerp(0.5f, 1f, 1f - pageOffset.coerceIn(0.1f..0.4f))
    val scaleX = 1f - pageOffset.coerceIn(0f..0.4f)
    val scaleY = 1f - pageOffset.coerceIn(0f..0.4f)

    Box(
        modifier = Modifier
            .graphicsLayer {
                this.alpha = alpha
                this.scaleX = scaleX
                this.scaleY = scaleY
                translationY = size.toPx() * pageOffset.coerceIn(0f..0.2f)
            }
    ) {
        IngredientItem(
            ingredient = ingredient,
            isSelected = isSelected,
            size = size,
            paddingValues = paddingValues,
            shape = shape,
            color = color,
            contentColor = contentColor,
            border = border,
            elevation = elevation,
            modifier = modifier
                .align(Alignment.Center)
        )

        AnimatedVisibility(
            visible = isSelected,
            enter = scaleIn(),
            exit = scaleOut(),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ActionRow(
                ingredient = ingredient,
                onAddClick = onAddClick,
                onRemoveClick = onRemoveClick,
                isFull = isFull,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun IngredientItem(
    ingredient: Ingredient,
    isSelected : Boolean,
    size: Dp,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    shape: Shape,
    color: Color,
    contentColor: Color,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
) {
    Surface(
        shape = shape,
        color = color,
        contentColor = contentColor,
        border = border,
        elevation = elevation,
        modifier = modifier
            .padding(paddingValues)
            .coloredShadow(
                color = ingredient.color,
                shadowRadius = if(isSelected) 4.dp else 0.dp
            )
            .padding(paddingValues)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .size(size)
                .padding(paddingValues)
        ) {
            Text(
                text = ingredient.emoji,
                style = MaterialTheme.typography.h4
            )
            Spacer(modifier = Modifier.padding(paddingValues))
            Text(
                text = ingredient.name,
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ActionRow(
    ingredient: Ingredient,
    onAddClick: () -> Unit,
    onRemoveClick: () -> Unit,
    isFull: Boolean,
    modifier : Modifier = Modifier,
    addText: String = if (ingredient.fullness == 0f) "add +" else "+",
    removeText: String = if (isFull) "- remove" else "-"
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {

        AnimatedVisibility(
            visible = !isFull,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            ActionItem(
                text = addText,
                onClick = onAddClick
            )
        }

        AnimatedVisibility(
            visible = ingredient.fullness > 0,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            ActionItem(
                text = removeText,
                onClick = onRemoveClick
            )
        }
    }
}