package net.limbuserendipity.customjuicecompose.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.graphics.scaleMatrix

class SvgShape(
    private val imageVector: ImageVector,
    private val shapeSize: Size,
    private val imageVectorSize: Size = Size(imageVector.viewportWidth, imageVector.viewportHeight)
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline = Outline.Generic(createPath())

    fun createPath(): Path {
        val parser = PathParser()
        parser.addPathNodes(imageVector.root.allPathNodes())

        val ratioX = shapeSize.width / imageVectorSize.width
        val ratioY = shapeSize.height / imageVectorSize.height
        val matrix = scaleMatrix(ratioX, ratioY)

        val path = parser.toPath()
        path.asAndroidPath().transform(matrix)
        path.translate(Offset(0f, 0f))
        return path
    }

}

@Composable
fun size(size: Dp): Size = size(size, size)

@Composable
fun size(weight: Dp, height: Dp): Size = with(LocalDensity.current) {
    Size(weight.toPx(), height.toPx())
}