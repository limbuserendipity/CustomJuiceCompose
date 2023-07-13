package net.limbuserendipity.customjuicecompose.ui.component

import android.graphics.DashPathEffect
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import net.limbuserendipity.customjuicecompose.ui.model.Ingredient
import net.limbuserendipity.customjuicecompose.ui.theme.juiceGreen
import kotlin.math.roundToInt

@Composable
fun Receipt(
    ingredients : List<Ingredient>,
    modifier : Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(8.dp)
){

    val separatorColor = LocalContentColor.current

    Column(
        modifier = modifier.padding(paddingValues)
    ) {

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .drawBehind { drawSeparator(separatorColor) }
        )

        ingredients.forEach { ingredient ->
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ){
                ReceiptItem(
                    ingredient = ingredient
                )

                Text(
                    text = ingredient.name.uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.body2,
                )

                Text(
                    text = "x${(ingredient.fullness * 10).roundToInt()}",
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.body2
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "0.00",
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.body2
                )
            }

        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .drawBehind { drawSeparator(separatorColor) }
        )

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = "TOTAL:",
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "0.00",
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.body1
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ){

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {  },
                colors = ButtonDefaults.buttonColors(backgroundColor = juiceGreen)
            ) {
                Text(text = "Buy")
            }

        }
    }
}

@Composable
fun ReceiptItem(
    ingredient: Ingredient,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    color: Color = MaterialTheme.colors.primary,
    contentColor: Color = contentColorFor(color),
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    paddingValues: PaddingValues = PaddingValues(8.dp)
){
    Surface(
        shape = shape,
        color = color,
        contentColor = contentColor,
        border = border,
        elevation = elevation,
        modifier = modifier.padding(paddingValues)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(paddingValues)
        ){
            Text(
                text = ingredient.emoji,
                style = MaterialTheme.typography.body1
            )
        }
    }
}

fun DrawScope.drawSeparator(
    color : Color,
    onInterval : Float = 16f,
    offInterval : Float = 16f,
    phase : Float = 16f
){

    val linePath = Path()
    linePath.moveTo(0f,center.y)
    linePath.lineTo(size.width, center.y)

    val dashedEffect = DashPathEffect(floatArrayOf(onInterval, offInterval), phase)

    val paint = Paint()
    paint.color = color
    paint.strokeWidth = 8f
    paint.style = PaintingStyle.Stroke
    paint.pathEffect = dashedEffect.toComposePathEffect()

    //drawPath(linePath, Color.White)

    drawContext.canvas.drawPath(linePath, paint)

}
