package puzzle12

import InputMapper
import Puzzle
import kotlin.math.abs

object Puzzle12 : Puzzle<Puzzle12.Input, Puzzle12.Output>(
    InputMapper { input ->
        val instructions = input.split("\n").map(Instruction::from)
        Input(instructions)
    }
) {
    override val inputFileName = "12.txt"

    override suspend fun doJob(input: Input): Output {
        return Output(
            moveShip(input, Ship(Position(0, 0), Direction.Est)),
            moveShip(input, Ship(Position(0, 0), Direction.Est, Position(10, -1))),
        )
    }

    private fun moveShip(input: Input, initialShip: Ship): Int {
        val ship = input.instructions.fold(initialShip) { ship, instruction ->
            instruction.run(ship).also { newShip ->
                println("Ship: $ship, instruction: $instruction --> $newShip")
            }
        }
        return ship.distanceTo(initialShip)
    }

    data class Input(
        val instructions: List<Instruction>
    )

    data class Output(
        val distanceWithoutWaypoint: Int,
        val distanceWithWaypoint: Int
    )
}

data class Position(val x: Int, val y: Int) {
    fun distanceTo(ship: Position) = abs(ship.x - x) + abs(ship.y - y)
    fun rotate(degrees: Int): Position {
        val rotations = abs(degrees / 90)
        var position = this
        repeat(rotations) {
            position = position.copy(
                x = -position.y,
                y = position.x
            )
        }
        return position
    }
}

data class Ship(
    val position: Position,
    val facing: Direction,
    val waypoint: Position? = null
) {
    fun distanceTo(ship: Ship) = position.distanceTo(ship.position)
}

enum class Direction {
    North,
    Est,
    South,
    West;

    fun turn(degrees: Int): Direction {
        val rotations = degrees / 90

        val values = values()
        val index = (ordinal + rotations) % values.size
        return values[index]
    }
}

sealed class Instruction {
    abstract fun run(ship: Ship): Ship
    sealed class Move : Instruction() {
        abstract val units: Int

        data class Forward(override val units: Int) : Move() {
            override fun run(ship: Ship): Ship {
                val (dX, dY) = if (ship.waypoint == null) {
                    when (ship.facing) {
                        Direction.North -> 0 to -units
                        Direction.South -> 0 to units
                        Direction.Est -> units to 0
                        Direction.West -> -units to 0
                    }
                } else {
                    ship.waypoint.x * units to ship.waypoint.y * units
                }
                val position = ship.position
                return ship.copy(position = position.copy(x = position.x + dX, y = position.y + dY))
            }
        }

        data class Toward(val direction: Direction, override val units: Int) : Move() {
            override fun run(ship: Ship): Ship {
                val (dX, dY) = when (direction) {
                    Direction.North -> 0 to -units
                    Direction.South -> 0 to units
                    Direction.Est -> units to 0
                    Direction.West -> -units to 0
                }
                return if (ship.waypoint == null) {
                    val position = ship.position
                    ship.copy(position = position.copy(x = position.x + dX, y = position.y + dY))
                } else {
                    val position = ship.waypoint
                    ship.copy(waypoint = position.copy(x = position.x + dX, y = position.y + dY))
                }
            }
        }
    }

    sealed class Turn(private val degrees: Int) : Instruction() {
        abstract val value: Int

        override fun run(ship: Ship): Ship {
            return if (ship.waypoint == null) {
                ship.copy(facing = ship.facing.turn(degrees))
            } else {
                val newWaypoint = ship.waypoint.rotate(degrees)
                ship.copy(waypoint = newWaypoint)
            }
        }

        data class Left(override val value: Int) : Turn(360 - value)
        data class Right(override val value: Int) : Turn(value)
    }

    companion object {
        private val REGEX = """(\w)(\d+)""".toRegex()
        fun from(instruction: String): Instruction {
            return REGEX.matchEntire(instruction)?.let { result ->
                val (action, value) = result.destructured
                when (action) {
                    "N" -> Move.Toward(Direction.North, value.toInt())
                    "S" -> Move.Toward(Direction.South, value.toInt())
                    "E" -> Move.Toward(Direction.Est, value.toInt())
                    "W" -> Move.Toward(Direction.West, value.toInt())
                    "F" -> Move.Forward(value.toInt())
                    "R" -> Turn.Right(value.toInt())
                    "L" -> Turn.Left(value.toInt())
                    else -> null
                }
            } ?: error("Invalid instruction")
        }
    }
}