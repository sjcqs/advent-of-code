package puzzle20

import readInput
import split

data class Point(val x: Int, val y: Int) {
    val lookupPixels = sequence {
        yield(Point(x - 1, y - 1))
        yield(Point(x, y - 1))
        yield(Point(x + 1, y - 1))
        yield(Point(x - 1, y))
        yield(Point(x, y))
        yield(Point(x + 1, y))
        yield(Point(x - 1, y + 1))
        yield(Point(x, y + 1))
        yield(Point(x + 1, y + 1))
    }
}

data class Input(
    val pixels: Map<Point, Char>, val width: Int, val height: Int, val lookupTable: Map<Int, Char>
)

fun lookupKey(point: Point, pixels: Map<Point, Char>, surroundingPixel: Char): Int {
    return point.lookupPixels.fold("") { acc, p ->
        acc + pixels.getOrDefault(p, surroundingPixel)
    }.toInt(2)
}

fun map(input: String): Input {
    val (lookupTableValue, image) = input.replace('.', '0').replace('#', '1').split("\n\n")
    val lookupTable = lookupTableValue.mapIndexed { index, c -> index to c }.toMap()

    val pixels = image.split().flatMapIndexed { y, row ->
        row.mapIndexed { x, c -> Point(x, y) to c }
    }.toMap()
    val width = pixels.keys.maxOf { it.x } + 1
    val height = pixels.keys.maxOf { it.y } + 1
    return Input(
        pixels = pixels, width = width, height = height, lookupTable = lookupTable
    )
}

fun enhance(input: Input, iterations: Int): Pair<Map<Point, Char>, Char> {
    val pixels = input.pixels
    val lookupTable = input.lookupTable
    var min = -1
    var width = input.width + 1
    var height = input.height + 1
    var surroundingPixel = '0'

    fun next(pixels: Map<Point, Char>): Map<Point, Char> {
        val newPixels = mutableMapOf<Point, Char>()
        for (y in min until height) {
            for (x in min until width) {
                val point = Point(x, y)
                val key = lookupKey(point, pixels, surroundingPixel)
                val newPixel = lookupTable.getValue(key)
                newPixels[point] = newPixel
            }
        }
        return newPixels
    }

    var newPixels = pixels
    repeat(iterations) {
        newPixels = next(newPixels)
        min--
        width++
        height++
        surroundingPixel = if (surroundingPixel == '0') lookupTable.getValue(0) else lookupTable.getValue(511)
    }
    return newPixels to surroundingPixel
}

fun debug(pixels: Map<Point, Char>, surroundingPixel: Char) {
    val minX = pixels.keys.minOf { it.x } - 1
    val minY = pixels.keys.minOf { it.y } - 1
    val width = pixels.keys.maxOf { it.x } + 1
    val height = pixels.keys.maxOf { it.y } + 1
    for (y in minY until height) {
        for (x in minX until width) {
            when (pixels.getOrDefault(Point(x, y), surroundingPixel)) {
                '0' -> print(".")
                '1' -> print("#")
                else -> print("?")
            }
        }
        println()
    }
}

fun main() {
    fun part01(input: Input): Int {
        val pixels = enhance(input, 2)
        return pixels.first.values.count { it == '1' }
    }

    fun part02(input: Input): Int {
        val pixels = enhance(input, 50)
        return pixels.first.values.count { it == '1' }
    }

    val example = readInput("example/20.txt").let(::map)
    val input = readInput("20.txt").let(::map)

    assert(part01(example) == 35) { "expected 35, got ${part01(example)}" }
    println("Part 01: ${part01(input)}")

    assert(part02(example) == 3351) { "expected 3351, got ${part02(example)}" }
    println("Part 02: ${part02(input)}")

    enhance(input, 75).let { (pixels, surroundingPixel) ->
        debug(pixels, surroundingPixel)
    }
}