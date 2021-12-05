package puzzle05

import readInput
import split
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    val example = readInput("example/05.txt").let(::map)
    val input = readInput("05.txt").let(::map)

    assert(part01(example) == 5) { "Part01: expected 5, got ${part01(example)}" }
    assert(part02(example) == 12) { "Part02: expected 12, got ${part02(example)}" }

    println("Part01: ${part01(input)}")
    println("Part02: ${part02(input)}")
}

private fun part01(input: List<Line>): Int {
    val lines = input.filter { it.p1.x == it.p2.x || it.p1.y == it.p2.y }
    val coveredPoints = coveredPoints(lines = lines, diagonals = false)
    return coveredPoints.values.count { it >= 2 }
}

private fun part02(input: List<Line>): Int {
    return coveredPoints(lines = input, diagonals = true).values.count { it >= 2 }
}

private fun coveredPoints(lines: List<Line>, diagonals: Boolean): Map<Point, Int> {
    val (maxX, maxY) = maxBound(lines)
    val intersectCount = (0..maxX).flatMap { x ->
        (0..maxY).map { y ->
            val p = Point(x, y)
            p to lines.count { line -> line.intersect(p, diagonals) }
        }
    }.toMap()
    return intersectCount
}

private fun maxBound(input: List<Line>) =
    input.fold(Int.MIN_VALUE to Int.MIN_VALUE) { (maxX, maxY), line ->
        max(maxX, max(line.p1.x, line.p2.x)) to max(maxY, max(line.p1.y, line.p2.y))
    }

private data class Point(val x: Int, val y: Int)

private data class Line(val p1: Point, val p2: Point) {
    fun intersect(p: Point, diagonals: Boolean): Boolean {
        if (p1.x == p2.x && p.x == p1.x) {
            return p.y in min(p1.y, p2.y)..max(p1.y, p2.y)
        }
        if (p1.y == p2.y && p.y == p1.y) {
            return p.x in min(p1.x, p2.x)..max(p1.x, p2.x)
        }

        if (diagonals) {
            val crossProduct = (p.y - p1.y) * (p2.x - p1.x) - (p.x - p1.x) * (p2.y - p1.y)

            if (abs(crossProduct) != 0) return false

            val dotProduct = (p.x - p1.x) * (p2.x - p1.x) + (p.y - p1.y) * (p2.y - p1.y)
            if (dotProduct < 0) return false

            val squaredLength = (p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y)

            if (dotProduct > squaredLength) return false

            return true
        } else return false
    }

}

private fun map(input: String): List<Line> {
    return input.trim().split().map {
        val (start, end) = it.split(" -> ")
        val (x1, y1) = start.split(",").map(String::toInt)
        val (x2, y2) = end.split(",").map(String::toInt)

        Line(Point(x1, y1), Point(x2, y2))
    }
}