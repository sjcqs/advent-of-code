package puzzle15

import readInput
import split

fun main() {
    data class Position(val x: Int, val y: Int) {
        fun getNeighbors(width: Int, height: Int) = listOfNotNull(
            Position(x + 1, y).takeIf { it.x <= width },
            Position(x, y + 1).takeIf { it.y <= height },
            Position(x - 1, y).takeIf { it.x >= 0 },
            Position(x, y - 1).takeIf { it.y >= 0 },
        )
    }

    data class Input(val nodes: Map<Position, Int>, val width: Int, val height: Int)

    fun map(input: String): Input {
        val nodes = input.split().flatMapIndexed { y, line ->
            line.mapIndexed { x, c ->
                Position(x, y) to c.digitToInt()
            }
        }.toMap()

        val width = nodes.keys.maxOf { it.x }
        val height = nodes.keys.maxOf { it.y }

        return Input(nodes, width, height)
    }

    fun solve(input: Input): Int {
        val distances = input.nodes.keys.associateWith { Int.MAX_VALUE }.toMutableMap()
        distances[Position(0, 0)] = 0
        val previousNode = mutableMapOf<Position, Position>()
        val toVisit = input.nodes.keys.toMutableList()
        while (toVisit.isNotEmpty()) {
            val position = toVisit.minByOrNull { distances.getValue(it) }!!
            val distance = distances.getValue(position)
            toVisit.remove(position)

            position.getNeighbors(input.width, input.height).filter { it in toVisit }.forEach { neighbor ->
                val risk = input.nodes.getValue(neighbor)
                if (distances.getValue(neighbor) > distance + risk) {
                    distances[neighbor] = distance + risk
                    previousNode[neighbor] = position
                }
            }
        }
        var currentNode = Position(input.width, input.height)
        val path = mutableListOf<Position>()
        while (currentNode != Position(0, 0)) {
            path.add(currentNode)
            currentNode = previousNode.getValue(currentNode)
        }

        return path.sumOf { input.nodes.getValue(it) }
    }

    fun Input.grow(factor: Int): Input {
        val newWidth = (width + 1) * factor
        val newHeight = (height + 1) * factor
        val map = nodes.toMutableMap()
        nodes.keys.forEach { (x, y) ->
            var newRisk = map.getValue(Position(x, y))
            (1 until factor).forEach { index ->
                newRisk++
                if (newRisk > 9) newRisk = 1
                val newPosition = Position(x + (index * (width + 1)), y)
                map[newPosition] = newRisk
            }
        }
        map.toMap().keys.forEach { (x, y) ->
            var newRisk = map.getValue(Position(x, y))
            (1 until factor).forEach { index ->
                newRisk++
                if (newRisk > 9) newRisk = 1
                val newPosition = Position(x, y + (index * (height + 1)))
                map[newPosition] = newRisk
            }
        }
        return Input(map, newWidth - 1, newHeight - 1)
    }


    val example = readInput("example/15.txt").let(::map)
    val input = readInput("15.txt").let(::map)

    assert(solve(example) == 40) { "expected 40, got ${solve(example)}" }
    println("Part 1: ${solve(input)}")

    val example2 = example.grow(5)
    val input2 = input.grow(5)
    assert(solve(example2) == 315) { "expected 315, got ${solve(example2)}" }
    println("Part 2: ${solve(input2)}")
}