package net.limbuserendipity.customjuicecompose.util

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.*

fun ImageVector.colorPath(pathName: String, brush: Brush): ImageVector {
    try {
        val path = root.findPath(pathName)
        val f = VectorPath::class.java.getDeclaredField("fill")
        f.isAccessible = true
        f.set(path, brush)
    }catch (e : Exception){
        e.printStackTrace()
    }

    return this
}

fun ImageVector.colorImageVector(brush: Brush): ImageVector {
    root.allNodes().forEach { node ->
        if(node is VectorPath){
            val f = VectorPath::class.java.getDeclaredField("fill")
            f.isAccessible = true
            f.set(node, brush)
        }
    }

    return this
}

fun VectorGroup.allNodes(): List<VectorNode> {
    val all = mutableListOf<VectorNode>()

    forEach { node ->
        when (node) {
            is VectorPath -> all.add(node)
            is VectorGroup -> all.addAll(node.allNodes())
        }
    }

    return all
}

fun VectorGroup.allPathNodes(): MutableList<PathNode> {
    val paths = mutableListOf<PathNode>()
    allNodes().forEach { node ->
        paths.addAll((node as VectorPath).pathData)
    }
    return paths
}

fun VectorGroup.findPath(pathName: String): VectorPath {
    return allNodes().first { node ->
        node is VectorPath && node.name == pathName
    } as VectorPath
}