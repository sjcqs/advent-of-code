package puzzle11

import readInput
import split

fun main() {
    val (width, height) = 10 to 10
    data class Position(val x: Int, val y: Int) {
        val adjacentPositions: Set<Position>
            get() = setOfNotNull(
                Position(x - 1, y - 1).takeIf { (x - 1) in 0 until width && (y - 1) in 0 until height },
                Position(x - 1, y).takeIf { (x - 1) in 0 until width && y in 0 until height },
                Position(x - 1, y + 1).takeIf { (x - 1) in 0 until width && (y + 1) in 0 until height },
                Position(x, y - 1).takeIf { (x) in 0 until width && (y - 1) in 0 until height },
                Position(x, y + 1).takeIf { (x) in 0 until width && (y + 1) in 0 until height },
                Position(x + 1, y - 1).takeIf { (x + 1) in 0 until width && (y - 1) in 0 until height },
                Position(x + 1, y).takeIf { (x + 1) in 0 until width && y in 0 until height },
                Position(x + 1, y + 1).takeIf { (x + 1) in 0 until width && (y + 1) in 0 until height },
            )
    }

    fun Map<Position, Int>.simulated() = generateSequence(this) { map ->
        val stepMap = map.toMutableMap()
        stepMap.replaceAll { _, level -> level + 1 }
        val flashedPositions = stepMap
            .mapNotNull { (position, level) -> position.takeIf { level > 9 } }
            .toMutableList()
        val visitedPositions = mutableListOf<Position>()
        while (flashedPositions.isNotEmpty()) {
            val position = flashedPositions.removeFirst()
            visitedPositions.add(position)

            position.adjacentPositions
                .filter { it !in visitedPositions && it !in flashedPositions }
                .forEach { adjacentPosition ->
                    stepMap[adjacentPosition] = stepMap.getValue(adjacentPosition) + 1
                    if (stepMap.getValue(adjacentPosition) > 9) {
                        flashedPositions.add(adjacentPosition)
                    }
                }
        }

        stepMap.replaceAll { _, level -> if (level > 9) 0 else level }
        stepMap
    }

    fun flashes(map: Map<Position, Int>, steps: Int): Int {
        if (steps == 0) return 0
        return map.simulated()
            .take(steps + 1)
            .sumOf { it.values.count { level -> level == 0 } }
    }

    fun firstSimultaneousFlash(map: Map<Position, Int>): Int {
        return map.simulated()
            .takeWhile { it.values.any { level -> level != 0 } }
            .count()
    }

    fun map(input: String): Map<Position, Int> {
        val lines = input.split()
        return lines.flatMapIndexed { y, line ->
            line.mapIndexed { x, c -> Position(x, y) to c.digitToInt() }
        }.toMap()
    }

    val example = readInput("example/11.txt").let(::map)
    val input = readInput("11.txt").let(::map)

    assert(flashes(example, 100) == 1656) { "expected: 1656, got ${flashes(example, 100)}" }
    println("${flashes(input, 100)}")

    assert(firstSimultaneousFlash(example) == 195) { "expected: 195, got ${firstSimultaneousFlash(example)}" }
    println("${firstSimultaneousFlash(input)}")


}