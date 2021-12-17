package puzzle17

import readInput
import kotlin.math.max

fun main() {
    data class Position(
        val x: Int,
        val y: Int,
    )

    data class Area(
        val xRange: IntRange,
        val yRange: IntRange,
    )

    fun Position.inArea(area: Area) = area.xRange.contains(x) && area.yRange.contains(y)

    fun Position.drag() = Position(
        if (x > 0) x - 1 else if (x < 0) x + 1 else 0,
        y - 1
    )

    fun Position.move(velocity: Position) = Position(
        x + velocity.x,
        y + velocity.y
    )

    fun solve(area: Area): Pair<Int, Int> {
        val origin = Position(0, 0)
        var maxHeight = Int.MIN_VALUE
        var count = 0
        (0..area.xRange.last).forEach { x ->
            (-area.yRange.first downTo area.yRange.first).forEach { y ->
                var position = origin
                var velocity = Position(x, y)
                var localMaxHeight = Int.MIN_VALUE
                var reachTarget = false
                while (!reachTarget && (velocity.x > 0 || position.x in area.xRange) && position.y >= area.yRange.first) {
                    localMaxHeight = max(localMaxHeight, position.y)
                    position = position.move(velocity)
                    velocity = velocity.drag()

                    if (position.inArea(area)) {
                        count++
                        maxHeight = max(localMaxHeight, maxHeight)
                        reachTarget = true
                    }
                }
            }
        }
        return maxHeight to count
    }

    fun map(input: String): Area {
        val regex = "target area: x=([-0-9]+)..([-0-9]+), y=([-0-9]+)..([-0-9]+)".toRegex()
        val (minX, maxX, minY, maxY) = (regex.matchEntire(input) ?: error("invalid input")).destructured
        return Area(minX.toInt()..maxX.toInt(), minY.toInt()..maxY.toInt())
    }

    val example = map("target area: x=20..30, y=-10..-5")
    val input = readInput("17.txt").let(::map)

    val result = solve(example)
    assert(result == 45 to 112) { "expected 45 and 112, got $result" }
    println("Part 01: ${solve(input)}")
}