package puzzle08

import readInput
import requireEquals
import split

fun main() {
    val example = grid(readInput("example/08.txt"))
    val input = grid(readInput("08.txt"))

    requireEquals(21, visibleTrees(example))
    println(visibleTrees(input))
    requireEquals(8, scenicScores(example).maxOf { (_, score) -> score })
    println(scenicScores(input).maxOf { (_, score) -> score })
}

private fun grid(input: String): Grid {
    val lines = input.trim().split()
    val width = lines.first().length
    val height = lines.size
    val sizes = lines.map { line -> line.map(Char::digitToInt) }

    return Grid(width, height, sizes)
}

private fun visibleTrees(grid: Grid): Int {
    var visibleCount = (grid.width * 2) + (grid.height * 2) - 4
    (1 until grid.height - 1).forEach { y ->
        (1 until grid.width - 1).forEach { x ->
            if (isVisibleFromLeft(x, y, grid) ||
                isVisibleFromTop(x, y, grid) ||
                isVisibleFromRight(x, y, grid) ||
                isVisibleFromBottom(x, y, grid)
            ) {
                visibleCount++
            }
        }
    }
    return visibleCount
}

private fun scenicScores(grid: Grid): Map<Pair<Int, Int>, Int> {
    return (1 until grid.height - 1).flatMap { y ->
        (1 until grid.width - 1).map { x ->
            val visibleFromTop = visibleFromTop(x, y, grid)
            val visibleFromLeft = visibleFromLeft(x, y, grid)
            val visibleFromRight = visibleFromRight(x, y, grid)
            val visibleFromBottom = visibleFromBottom(x, y, grid)
            Pair(x, y) to (visibleFromLeft * visibleFromTop * visibleFromRight * visibleFromBottom)
        }
    }.toMap()
}

private fun visibleFromTop(x: Int, y: Int, grid: Grid): Int {
    val sizes = grid.sizes
    val size = sizes[y][x]
    val visible = (y - 1 downTo 0).takeWhile { topY ->
        sizes[topY][x] < size
    }
    return visible.size + if (visible.all { it > 0 }) 1 else 0
}

private fun visibleFromLeft(x: Int, y: Int, grid: Grid): Int {
    val sizes = grid.sizes
    val size = sizes[y][x]
    val visible = (x - 1 downTo 0).takeWhile { leftX ->
        (sizes[y][leftX] < size)
    }
    return visible.size + if (visible.all { it > 0 }) 1 else 0
}

private fun visibleFromRight(x: Int, y: Int, grid: Grid): Int {
    val sizes = grid.sizes
    val size = sizes[y][x]
    val visible = (x + 1 until grid.width).takeWhile { rightX ->
        sizes[y][rightX] < size
    }
    return visible.size + if (visible.all { it < grid.width - 1 }) 1 else 0
}

private fun visibleFromBottom(x: Int, y: Int, grid: Grid): Int {
    val sizes = grid.sizes
    val size = sizes[y][x]
    val visible = (y + 1 until grid.height).takeWhile { rightY ->
        sizes[rightY][x] < size
    }
    return visible.size + if (visible.all { it < grid.height - 1 }) 1 else 0
}

private fun isVisibleFromTop(x: Int, y: Int, grid: Grid): Boolean {
    val sizes = grid.sizes
    val size = sizes[y][x]
    return (y - 1 downTo 0).all { topY ->
        sizes[topY][x] < size
    }
}

private fun isVisibleFromLeft(x: Int, y: Int, grid: Grid): Boolean {
    val sizes = grid.sizes
    val size = sizes[y][x]
    return (x - 1 downTo 0).all { leftX ->
        (sizes[y][leftX] < size)
    }
}

private fun isVisibleFromRight(x: Int, y: Int, grid: Grid): Boolean {
    val sizes = grid.sizes
    val size = sizes[y][x]
    return (x + 1 until grid.width).all { rightX ->
        sizes[y][rightX] < size
    }
}

private fun isVisibleFromBottom(x: Int, y: Int, grid: Grid): Boolean {
    val sizes = grid.sizes
    val size = sizes[y][x]
    return (y + 1 until grid.height).all { rightY ->
        sizes[rightY][x] < size
    }
}

private data class Grid(
    val width: Int,
    val height: Int,
    val sizes: List<List<Int>>
) {
    override fun toString(): String {
        return buildString {
            appendLine("width: $width")
            appendLine("width: $height")
            appendLine("grid:")
            sizes.forEach { line ->
                line.forEach {
                    append(it)
                }
                appendLine()
            }
        }
    }
}