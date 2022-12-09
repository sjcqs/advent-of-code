package puzzle09

import Position
import readInput
import requireEquals
import split

private enum class Direction {
    Left, Up, Right, Down
}

private data class Instruction(
    val direction: Direction,
    val count: Int
) {
    companion object {
        fun parse(value: String): Instruction {
            val (letter, count) = value.split(" ")
            val direction = when (letter) {
                "R" -> Direction.Right
                "L" -> Direction.Left
                "U" -> Direction.Up
                "D" -> Direction.Down
                else -> error("Invalid instruction $letter")
            }

            return Instruction(direction, count.toInt())
        }
    }
}

fun main() {
    val example = readInput("example/09.txt").trim().split().map(Instruction.Companion::parse)
    val example1 = readInput("example/09bis.txt").trim().split().map(Instruction.Companion::parse)
    val input = readInput("09.txt").trim().split().map(Instruction.Companion::parse)

    requireEquals(13, positionVisitedByTail(example).size)
    println(positionVisitedByTail(input).size)

    requireEquals(1, positionVisitedByTail(example, 10).size)
    requireEquals(36, positionVisitedByTail(example1, 10).size)
    println(positionVisitedByTail(input, 10).size)
}

private fun Position.isTouching(position: Position): Boolean {
    return x in (position.x - 1..position.x + 1) && y in (position.y - 1..position.y + 1)
}

private fun Position.moveToward(position: Position): Position {
    if (isTouching(position)) {
        return this
    }
    return when {
        x == position.x -> {
            copy(
                y = if (y > position.y) {
                    y - 1
                } else {
                    y + 1
                }
            )
        }

        y == position.y -> {
            copy(
                x = if (x > position.x) {
                    x - 1
                } else {
                    x + 1
                }
            )
        }

        else -> when {
            x > position.x && y > position.y -> {
                copy(x = x - 1, y = y - 1)
            }

            x > position.x /*&& y < position.y*/ -> {
                copy(x = x - 1, y = y + 1)
            }

            /*x < position.x &&*/ y > position.y -> {
                copy(x = x + 1, y = y - 1)
            }

            /*x < position.x && y < position.y*/ else -> {
                copy(x = x + 1, y = y + 1)
            }
        }
    }
}

private fun positionVisitedByTail(instructions: List<Instruction>, knotCount: Int = 2): Set<Position> {
    val knotPositions = MutableList(knotCount) {
        Position(0, 0)
    }
    return buildSet {
        add(knotPositions.last())
        instructions.forEach { instruction ->
            repeat(instruction.count) {
                val headPosition = knotPositions.first()
                val newHeadPosition = when (instruction.direction) {
                    Direction.Left -> {
                        headPosition.copy(
                            x = headPosition.x - 1
                        )
                    }

                    Direction.Up -> {
                        headPosition.copy(
                            y = headPosition.y + 1
                        )
                    }

                    Direction.Right -> {
                        headPosition.copy(
                            x = headPosition.x + 1
                        )
                    }

                    Direction.Down -> {
                        headPosition.copy(
                            y = headPosition.y - 1
                        )
                    }
                }
                knotPositions[0] = newHeadPosition
                knotPositions.drop(1).scanIndexed(newHeadPosition) { index, head, tail ->
                    tail.moveToward(head).also {
                        knotPositions[index + 1] = it
                    }
                }
                add(knotPositions.last())
            }
        }
    }
}

private fun debug(headPosition: Position, tailPosition: Position) {
    val size = 6
    repeat(size) {
        val y = (size - it - 1)
        repeat(size) { x ->
            val position = Position(x, y)
            when (position) {
                headPosition -> print("H")
                tailPosition -> print("T")
                else -> print(".")
            }
        }
        println()
    }
    println()
}