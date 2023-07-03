package net.limbuserendipity.customjuicecompose.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
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
    onItemClick: (Int,Ingredient) -> Unit,
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
        val item = ingredients[index]
        IngredientCard(
            ingredient = item,
            onClick = { onItemClick(index,item) },
            size = pageSize,
            modifier = pageModifier
                .graphicsLayer {
                    val pageOffset = ((pagerState.currentPage - index) +
                            pagerState.currentPageOffsetFraction).absoluteValue

                    alpha = lerp(0.5f, 1f, 1f - pageOffset.coerceIn(0.1f..0.4f))

                    scaleX = 1f - pageOffset.coerceIn(0f..0.4f)
                    scaleY = 1f - pageOffset.coerceIn(0f..0.4f)

                    translationY = pageSize.toPx() * pageOffset.coerceIn(0f..0.2f)

                }
        )
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IngredientCard(
    ingredient: Ingredient,
    onClick: () -> Unit,
    size: Dp,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(4.dp),
    shape: Shape = CircleShape,
    color: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(color),
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
            .padding(paddingValues)
            .coloredShadow(ingredient.color)
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
