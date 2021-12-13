package puzzle13

import readInput
import kotlin.math.abs

fun main() {
    data class Point(val x: Int, val y: Int) {
        fun foldOn(fold: Point): Point {
            return when {
                fold.x < x -> {
                    copy(x = abs(x - (2 * fold.x)))
                }
                fold.y < y -> {
                    copy(y = abs(y - (2 * fold.y)))
                }
                else -> this
            }
        }
    }

    data class Input(
        val paper: Set<Point>,
        val folds: List<Point>,
        val width: Int,
        val height: Int,
    )

    fun map(input: String): Input {
        val (points, instructions) = input.split("\n\n")
        var width = Int.MIN_VALUE
        var height = Int.MIN_VALUE
        val paper = points.lines().map { line ->
            val (x, y) = line.split(",")
            if (x.toInt() > width) width = x.toInt()
            if (y.toInt() > height) height = y.toInt()
            Point(x.toInt(), y.toInt())
        }.toSet()
        val folds = instructions.lines().map { line ->
            val (axis, value) = line.split("=")
            if (axis.last() == 'x') {
                Point(value.toInt(), Int.MAX_VALUE)
            } else {
                Point(Int.MAX_VALUE, value.toInt())
            }
        }
        return Input(paper, folds, width + 1, height + 1)
    }

    fun foldPaper(input: Input, times: Int = input.folds.size): Set<Point> {
        var paper = input.paper
        input.folds.take(times).forEach { fold ->
            paper = paper.map { point ->
                point.foldOn(fold)
            }.toSet()
        }
        return paper
    }

    val example = readInput("example/13.txt").let(::map)
    val input = readInput("13.txt").let(::map)

    assert(foldPaper(example, 1).size == 17) { "expected 17; got ${foldPaper(example, 1)}" }
    println("part 1: ${foldPaper(input, 1).size}")

    println("part 2:")
    val foldedPaper = foldPaper(input)
    for (y in 0..foldedPaper.maxOf { it.y }) {
        for (x in 0..foldedPaper.maxOf { it.x }) {
            if (foldedPaper.contains(Point(x, y))) {
                print('#')
            } else {
                print(' ')
            }
        }
        println()
    }
}