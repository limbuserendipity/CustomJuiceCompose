package net.limbuserendipity.customjuicecompose.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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
    cupFullness: Float,
    modifier: Modifier = Modifier
){

    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {

        val weight = maxWidth * 3 / 4
        val height = weight / 3 * 4

        val shape = SvgShape(
            imageVector = imageShape,
            shapeSize = size(weight, height),
            imageVectorSize = Size(14f, 24f)
        )

        JuiceColumn(
            ingredients = ingredients,
            maxHeight = height,
            cupFullness = cupFullness,
            modifier = Modifier
                .size(weight, height)
                .border(
                    BorderStroke(4.dp, Color.White), shape
                )
                .clip(shape)
        )

        Image(
            imageVector = imageForeground,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            alpha = 0.3f,
            modifier = Modifier.size(weight, height)
        )
    }
}

@Composable
fun JuiceColumn(
    ingredients: List<Ingredient>,
    maxHeight: Dp,
    cupFullness: Float,
    modifier: Modifier = Modifier,
    density: Density = LocalDensity.current
) {

    val voidSize = with(density) {
        (maxHeight.toPx() - cupFullness * maxHeight.toPx()).toDp()
    }

    Column(
        modifier = modifier
    ) {

        Spacer(modifier = Modifier.size(voidSize))

        var previousColor = Color.White

        ingredients.forEach { ingredient ->

            val ingredientHeight = with(density) {
                (ingredient.fullness * maxHeight.toPx()).toDp()
            }

            val gradientColor = Brush.verticalGradient(listOf(previousColor, ingredient.color))

            JuiceItem(
                color = SolidColor(ingredient.color.copy(alpha = 0.8f)),
                height = ingredientHeight,
                modifier = Modifier.fillMaxWidth()
            )

            previousColor = ingredient.color

        }

    }
}

@Composable
fun JuiceItem(
    color: Brush,
    height: Dp,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .height(height)
            .background(color)
    )

}