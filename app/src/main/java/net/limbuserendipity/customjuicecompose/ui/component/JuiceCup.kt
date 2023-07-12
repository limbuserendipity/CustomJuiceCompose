package net.limbuserendipity.customjuicecompose.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
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
import androidx.core.graphics.ColorUtils
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
    isCompleted: Boolean,
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
            isCompleted = isCompleted,
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
    isCompleted: Boolean,
    modifier: Modifier = Modifier,
    density: Density = LocalDensity.current
) {

    val generalColor = if (isCompleted) {
        ingredients.foldRight(Color.White) { item, sum ->
            Color(ColorUtils.blendARGB(sum.toArgb(), item.color.toArgb(), item.fullness))
        }
    } else Color.White

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {

        for (i in ingredients.lastIndex downTo 0) {
            val ingredient = ingredients[i]
            var ingredientHeight by remember { mutableStateOf(0.dp) }
            var ovalHeight by remember { mutableStateOf(0f) }
            val animateHeight = animateDpAsState(
                targetValue = ingredientHeight,
                animationSpec = defaultProgressAnimationSpec()
            )

            val topHeight: Float
            with(density) {
                ingredientHeight = (ingredient.fullness * maxHeight.toPx()).toDp()
                topHeight = ingredients.foldRightIndexed(0f) { index, item, sum ->
                        if (index <= i) sum + item.fullness else sum
                    } * maxHeight.toPx()
            }

            val animateOvalHeight = animateFloatAsState(
                targetValue = ovalHeight,
                animationSpec = defaultProgressAnimationSpec()
            )

            ovalHeight = 36f

            val color = if (isCompleted) generalColor else ingredient.color
            val animateColor = animateColorAsState(
                targetValue = color,
                animationSpec = defaultProgressAnimationSpec(1000)
            )
            val nextColor = if (isCompleted) generalColor else{
                if (i != ingredients.lastIndex) ingredients[i + 1].color
                else lerp(animateColor.value, Color.Gray, 0.1f)
            }
            val animateNextColor = animateColorAsState(
                targetValue = nextColor,
                animationSpec = defaultProgressAnimationSpec(1000)
            )
            val ovalColor = if(isCompleted || cupFullness == 1f){
                animateColor.value
            }else{
                if (i != ingredients.lastIndex) animateNextColor.value
                else lerp(animateColor.value, Color.Gray, 0.1f)
            }

            JuiceItem(
                color = color,
                onClick = { onItemClick(ingredient) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(animateHeight.value)
                    .background(animateColor.value, RectangleShape)
                    .drawBehind {
                        drawTopOval(
                            color = ovalColor,
                            svgPath = svgPath,
                            maxWidth = maxWidth.toPx(),
                            maxHeight = maxHeight.toPx(),
                            topHeight = topHeight,
                            ovalHeight = animateOvalHeight.value
                        )
                    }
            )
        }
    }

}

@Composable
fun JuiceItem(
    color : Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = rememberRipple(
                    bounded = false,
                    radius = 124.dp,
                    color = color
                ),
                enabled = true,
                onClick = onClick
            )
    )
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
    maxWidth: Float,
    maxHeight: Float,
    top: Float,
    bottom: Float
): Rect {
    val rectPath = Path()
    rectPath.moveTo(0f, maxHeight - top)
    rectPath.lineTo(maxWidth, maxHeight - top)
    rectPath.lineTo(maxWidth, maxHeight - bottom)
    rectPath.lineTo(0f, maxHeight - bottom)
    rectPath.lineTo(0f, maxHeight - top)
    val newPath = Path.combine(PathOperation.Intersect, rectPath, path)
    return newPath.getBounds()
}