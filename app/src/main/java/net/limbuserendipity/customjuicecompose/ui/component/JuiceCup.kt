package net.limbuserendipity.customjuicecompose.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
            shapeSize = size(width, height),
            imageVectorSize = Size(14f, 24f)
        )

        JuiceColumn(
            ingredients = ingredients,
            onItemClick = onIngredientClick,
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
    maxWeight: Dp,
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

        ingredients
            .filter { it.fullness > 0f }
            .sortedBy { it.fullness }
            .forEachIndexed { index, ingredient ->

                val ingredientHeight = with(density) {
                    (ingredient.fullness * maxHeight.toPx()).toDp()
                }
                val animateHeight = animateDpAsState(targetValue = ingredientHeight)

                JuiceItem(
                    onClick = { onItemClick(ingredient) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(animateHeight.value)
                        .background(ingredient.color, RectangleShape)
                        .drawBehind {
                            if (index == 0 && cupFullness != 1f) {
                                val bottomWidth = with(density) {
                                    maxWeight.toPx() * 12f / 14f
                                }
                                val desiredHeight = cupFullness * maxHeight.toPx()
                                val widthRatio = (size.width - bottomWidth) / (maxHeight.toPx())
                                val lineWidth = bottomWidth + (desiredHeight * widthRatio)

                                drawTopOval(
                                    color = lerp(ingredient.color, Color.Gray, 0.1f),
                                    width = lineWidth
                                )
                            }
                        }
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

fun DrawScope.drawTopOval(
    color: Color,
    width: Float
) {
    val paint = Paint()
    paint.color = color
    paint.style = PaintingStyle.Fill
    drawContext.canvas.drawOval(
        left = size.width - width,
        top = 40f,
        right = width,
        bottom = -20f,
        paint = paint
    )
}