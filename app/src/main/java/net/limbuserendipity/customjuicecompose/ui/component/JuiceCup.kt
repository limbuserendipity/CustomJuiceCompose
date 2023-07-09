package net.limbuserendipity.customjuicecompose.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
            maxWidth = width,
            maxHeight = height,
            cupFullness = cupFullness,
            modifier = Modifier
                .size(width, height)
                .border(
                    BorderStroke(4.dp, Color.White), shape
                )
                .clip(shape)
        )

        Image(
            imageVector = imageForeground,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            alpha = 0.1f,
            modifier = Modifier.size(width, height)
        )
    }
}

@Composable
fun JuiceColumn(
    ingredients: List<Ingredient>,
    onItemClick: (Ingredient) -> Unit,
    svgPath: Path,
    maxWidth: Dp,
    maxHeight: Dp,
    cupFullness: Float,
    modifier: Modifier = Modifier,
    density: Density = LocalDensity.current
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {

        for (i in ingredients.lastIndex downTo 0) {
            val ingredient = ingredients[i]
            val ingredientHeight: Dp
            val topHeight: Float
            with(density) {
                ingredientHeight = (ingredient.fullness * maxHeight.toPx()).toDp()
                topHeight = ingredients
                    .map { it.fullness }.foldRightIndexed(0f){ index, item, sum ->
                        if(index <= i) sum + item else sum
                    } * maxHeight.toPx()
            }
            val animateHeight = animateDpAsState(
                targetValue = ingredientHeight,
                animationSpec = defaultProgressAnimationSpec()
            )

            val ovalHeight = 30f

            JuiceItem(
                text = "$topHeight & ${ingredient.fullness}",
                onClick = { onItemClick(ingredient) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(animateHeight.value)
                    .background(ingredient.color, RectangleShape)
                    .drawBehind {
                        drawTopOval(
                            color = if (i == ingredients.lastIndex)
                                lerp(ingredient.color, Color.Gray, 0.1f)
                            else ingredient.color,
                            svgPath = svgPath,
                            maxWidth = maxWidth.toPx(),
                            maxHeight = maxHeight.toPx(),
                            topHeight = topHeight,
                            ovalHeight = ovalHeight
                        )
                    }
            )
        }
    }

}

@Composable
fun JuiceItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clickable(onClick = onClick)
    ) {
        Text(text = text)
    }
}

fun DrawScope.drawTopOval(
    color: Color,
    svgPath: Path,
    maxWidth: Float,
    maxHeight: Float,
    topHeight: Float,
    ovalHeight: Float
) {
    val bounds = getBoundsInPathByHeight(
        svgPath,
        maxWidth,
        maxHeight,
        topHeight - ovalHeight,
        topHeight,
    )
    val ovalPath = Path()
    ovalPath.addOval(
        oval = Rect(
            bounds.left,
            -ovalHeight / 2,
            bounds.right,
            ovalHeight
        )
    )
    drawPath(
        path = ovalPath,
        color = color
    )
}

fun getBoundsInPathByHeight(
    path: Path,
    maxWidth : Float,
    maxHeight : Float,
    top : Float,
    bottom : Float
) : Rect{
    val rectPath = Path()
    rectPath.moveTo(0f,maxHeight - top)
    rectPath.lineTo(maxWidth,maxHeight - top)
    rectPath.lineTo(maxWidth,maxHeight - bottom)
    rectPath.lineTo(0f,maxHeight - bottom)
    rectPath.lineTo(0f,maxHeight - top)
    val newPath = Path.combine(PathOperation.Intersect, rectPath, path)
    return newPath.getBounds()
}