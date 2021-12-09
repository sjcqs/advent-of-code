package puzzle09

import readInput
import split

fun main() {
    data class Position(val x: Int, val y: Int) {
        fun adjacent(): List<Position> = listOf(
            Position(x - 1, y),
            Position(x + 1, y),
            Position(x, y - 1),
            Position(x, y + 1),
        )
    }

    data class Heatmap(
        val width: Int,
        val height: Int,
        val heats: Map<Position, Int>
    ) : Map<Position, Int> by heats

    fun map(input: String): Heatmap {
        val rows = input.split()
        val map = rows
            .flatMapIndexed { y, row ->
                row.mapIndexed { x, number ->
                    Position(x, y) to number.digitToInt()
                }
            }.toMap()

        return Heatmap(rows.first().length, rows.size, map)
    }

    fun Heatmap.lowestPoints(): Collection<Position> {
        return (0 until height).flatMap { y ->
            (0 until width).mapNotNull { x ->
                val position = Position(x, y)
                val adjacentValues = position.adjacent().mapNotNull { heats[it] }
                val value = heats[position] ?: Int.MAX_VALUE

                position.takeIf { adjacentValues.all { it > value } }
            }

        }
    }

    fun Heatmap.basin(currentPosition: Position): Set<Position> {
        val visited = mutableSetOf<Position>()
        val toVisit = mutableListOf(currentPosition)

        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            toVisit += current.adjacent().filter {
                val heat = heats.getOrDefault(it, 9)
                heat != 9 &&
                    it !in visited &&
                    it !in toVisit &&
                    heat > heats.getValue(current)
            }
            visited += current
        }
        return visited
    }

    fun part01(map: Heatmap): Int {
        return map.lowestPoints().sumOf { map.getValue(it) + 1 }
    }

    fun part02(map: Heatmap): Int {
        val lowestPoints = map.lowestPoints()
        val basins = lowestPoints.map { map.basin(it) }
        return basins
            .map { it.size }
            .sortedDescending()
            .take(3)
            .fold(1) { product, size -> product * size }
    }

    val example = readInput("example/09.txt").let(::map)
    val input = readInput("09.txt").let(::map)

    assert(part01(example) == 15) { "part01(example) should be 15, got ${part01(example)}" }
    println(part01(input))

    assert(part02(example) == 1134) { "part02(example) should be 1134, got ${part02(example)}" }
    println(part02(input))
}