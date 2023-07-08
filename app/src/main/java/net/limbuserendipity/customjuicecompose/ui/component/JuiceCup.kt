package net.limbuserendipity.customjuicecompose.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.translationMatrix
import net.limbuserendipity.customjuicecompose.ui.model.Ingredient
import net.limbuserendipity.customjuicecompose.util.SvgShape
import net.limbuserendipity.customjuicecompose.util.size

@Composable
fun JuiceCup(
    imageShape: ImageVector,
    imageForeground: ImageVector,
    ingredients: List<Ingredient>,
    onIngredientClick: (Ingredient) -> Unit,
    cupFullness: Float,
    modifier: Modifier = Modifier
) {

    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {

        val width = maxWidth * 3 / 4
        val height = width / 3 * 4

        val shape = SvgShape(
            imageVector = imageShape,
            shapeSize = size(width, height)
        )

        JuiceColumn(
            ingredients = ingredients,
            onItemClick = onIngredientClick,
            svgPath = shape.createPath(),
            maxWeight = width,
            maxHeight = height,
            cupFullness = cupFullness,
            modifier = Modifier
                .size(width, height)
                .border(
                    BorderStroke(2.dp, Color.White), shape
                )
                .clip(shape)
        )

        Image(
            imageVector = imageForeground,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            alpha = 0.3f,
            modifier = Modifier.size(width, height)
        )
    }
}

@Composable
fun JuiceColumn(
    ingredients: List<Ingredient>,
    onItemClick: (Ingredient) -> Unit,
    svgPath: Path,
    maxWeight: Dp,
    maxHeight: Dp,
    cupFullness: Float,
    modifier: Modifier = Modifier,
    density: Density = LocalDensity.current
) {

    val animateFullness = animateFloatAsState(
        targetValue = cupFullness,
        animationSpec = defaultProgressAnimationSpec()
    )

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .drawWithContent {
                drawContent()
                val rectPath = Path()
                val h = animateFullness.value * size.height
                val rH = maxHeight.toPx() - h
                rectPath.addRect(
                    rect = Rect(0f, rH + 30f, size.width, rH - 30f)
                )
                val newPath = Path.combine(PathOperation.Intersect, rectPath, svgPath)
                val ovalPath = Path()
                ovalPath.addOval(
                    oval = newPath.getBounds()
                )
                if (!ingredients.isEmpty())
                    drawPath(
                        path = ovalPath,
                        color = lerp(ingredients.last().color, Color.Gray, 0.1f)
                    )

            }
    ) {
        for (i in ingredients.lastIndex downTo 0) {
            val ingredient = ingredients[i]
            val ingredientHeight = with(density) {
                (ingredient.fullness * maxHeight.toPx()).toDp()
            }
            val animateHeight = animateDpAsState(
                targetValue = ingredientHeight,
                animationSpec = defaultProgressAnimationSpec()
            )

            JuiceItem(
                onClick = { onItemClick(ingredient) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(animateHeight.value)
                    .background(ingredient.color, RectangleShape)
            )
        }
    }

}

@Composable
fun JuiceItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
    )
}
