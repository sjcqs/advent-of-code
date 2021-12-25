package puzzle25

import readInput
import split

data class Position(val x: Int, val y: Int)
data class Cucumber(val dx: Int = 0, val dy: Int = 0) {
    fun print() {
        when {
            dx > 0 -> print('>')
            dy > 0 -> print('v')
            else -> Unit /* no-op */
        }
    }
}

data class Seafloor(
    val map: Map<Position, Cucumber>,
    val width: Int,
    val height: Int,
) {
    private val movableEastHerd = map.filter { (position, cucumber) ->
        cucumber.dx != 0 && map[Position((position.x + cucumber.dx) % width, position.y)] == null
    }

    private val movableSouthHerd = map.filter { (position, cucumber) ->
        cucumber.dy != 0 && map[Position(position.x, (position.y + cucumber.dy) % height)] == null
    }

    val isStable: Boolean = movableEastHerd.isEmpty() && movableSouthHerd.isEmpty()

    fun step(): Seafloor {
        val newMap = map.toMutableMap()

        fun moveEastHerd(): Seafloor {
            movableEastHerd.forEach { (position, cucumber) ->
                newMap[Position((position.x + cucumber.dx) % width, position.y)] = cucumber
                newMap.remove(position)
            }
            return copy(map = newMap)
        }

        fun Seafloor.moveSouthHerd(): Seafloor {
            map.filter { (position, cucumber) ->
                cucumber.dy != 0 && map[Position(position.x, (position.y + cucumber.dy) % height)] == null
            }.forEach { (position, cucumber) ->
                newMap[Position(position.x, (position.y + cucumber.dy) % height)] = cucumber
                newMap.remove(position)
            }
            return copy(map = newMap)
        }

        return moveEastHerd().moveSouthHerd()
    }

    fun print() {
        for (y in 0 until height) {
            for (x in 0 until width) {
                val cucumber = map[Position(x, y)]
                if (cucumber != null) {
                    cucumber.print()
                } else {
                    print(".")
                }
            }
            println()
        }
    }
}

fun parse(input: String): Seafloor {
    val map = input.split().flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, c ->
            when (c) {
                '>' -> Position(x, y) to Cucumber(1, 0)
                'v' -> Position(x, y) to Cucumber(0, 1)
                else -> null
            }
        }
    }.toMap()
    return Seafloor(map, map.keys.maxOf { it.x } + 1, map.keys.maxOf { it.y } + 1)
}

fun part01(initialSeafloor: Seafloor): Int {
    var seafloor = initialSeafloor
    var steps = 1
    while (!seafloor.isStable) {
        steps++
        seafloor = seafloor.step()
    }
    return steps
}

fun main() {
    val example = parse(readInput("example/25.txt"))
    val input = parse(readInput("25.txt"))
    assert(part01(example) == 58) { "expected 58, got ${part01(example)}" }

    println("Part 01: ${part01(input)}")
}