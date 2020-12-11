package puzzle11

import InputMapper
import Puzzle
import kotlin.math.max

object Puzzle11 : Puzzle<Puzzle11.Input, Puzzle11.Output>(InputMapper { input ->
    val state = input.split("\n")
        .map { row -> row.trim().map(Position::from) }
    Input(SeatLayout(state))
}) {
    override val inputFileName = "11.txt"

    override suspend fun doJob(input: Input): Output {
        return Output(
            mapOf(
                SimulationRule.Adjacent to input.layout.simulate(SimulationRule.Adjacent).occupiedSeats,
                SimulationRule.Visible to input.layout.simulate(SimulationRule.Visible).occupiedSeats
            )
        )
    }

    data class Input(val layout: SeatLayout)

    data class Output(val occupiedSeats: Map<SimulationRule, Int>)
}

enum class SimulationRule(val maxOccupied: Int) {
    Adjacent(4), Visible(5)
}

data class SeatLayout(val state: List<List<Position>>) {
    val occupiedSeats: Int = state.sumOf { row -> row.count { it == Position.Occupied } }
    fun simulate(rule: SimulationRule = SimulationRule.Adjacent): SeatLayout {
        val newState = step(rule)
        return if (newState == state) {
            this
        } else {
            copy(state = newState).simulate(rule)
        }
    }

    private fun step(rule: SimulationRule): List<List<Position>> {
        val maxOccupied = rule.maxOccupied
        return state.mapIndexed { y, row ->
            row.mapIndexed { x, position ->
                if (position == Position.Floor) {
                    Position.Floor
                } else {
                    val adjacentPositions = checkedPositions(rule, x, y)
                    when (position) {
                        Position.Empty -> emptySeatRule(adjacentPositions)
                        Position.Occupied -> occupiedSeatRule(maxOccupied, adjacentPositions)
                        Position.Floor -> Position.Floor
                    }
                }
            }
        }
    }

    private fun checkedPositions(rule: SimulationRule, x: Int, y: Int): List<Position> {
        return when (rule) {
            SimulationRule.Adjacent -> adjacentPositions(x, y)
            SimulationRule.Visible -> visiblePosition(x, y)
        }
    }

    private fun visiblePosition(x: Int, y: Int): List<Position> {
        val deltaMax = max(state.size, state.first().size)
        return listOfNotNull(
            firstEstPosition(deltaMax, y, x),
            firstWestPosition(deltaMax, y, x),
            firstSouthPosition(deltaMax, y, x),
            firstNorthPosition(deltaMax, y, x),
            firstSouthEstPosition(deltaMax, y, x),
            firstSouthWestPosition(deltaMax, y, x),
            firstNorthEstPosition(deltaMax, y, x),
            firstNorthWestPosition(deltaMax, y, x),
        )
    }

    private fun firstEstPosition(deltaMax: Int, y: Int, x: Int): Position? {
        for (delta in 1 until deltaMax) {
            val position = state.getOrNull(y)?.getOrNull(x + delta)
            if (position != null && position.isSeat) {
                return position
            }
        }
        return null
    }

    private fun firstWestPosition(deltaMax: Int, y: Int, x: Int): Position? {
        for (delta in 1 until deltaMax) {
            val position = state.getOrNull(y)?.getOrNull(x - delta)
            if (position != null && position.isSeat) {
                return position
            }
        }
        return null

    }

    private fun firstSouthPosition(deltaMax: Int, y: Int, x: Int): Position? {
        for (delta in 1 until deltaMax) {
            val position = state.getOrNull(y + delta)?.getOrNull(x)
            if (position != null && position.isSeat) {
                return position
            }
        }
        return null
    }

    private fun firstNorthPosition(deltaMax: Int, y: Int, x: Int): Position? {
        for (delta in 1 until deltaMax) {
            val position = state.getOrNull(y - delta)?.getOrNull(x)
            if (position != null && position.isSeat) {
                return position
            }
        }
        return null
    }

    private fun firstSouthEstPosition(deltaMax: Int, y: Int, x: Int): Position? {
        for (delta in (1..deltaMax)) {
            val position = state.getOrNull(y + delta)?.getOrNull(x + delta)
            if (position != null && position.isSeat) {
                return position
            }
        }
        return null
    }

    private fun firstSouthWestPosition(deltaMax: Int, y: Int, x: Int): Position? {
        for (delta in (1..deltaMax)) {
            val position = state.getOrNull(y + delta)?.getOrNull(x - delta)
            if (position != null && position.isSeat) {
                return position
            }
        }
        return null
    }

    private fun firstNorthEstPosition(deltaMax: Int, y: Int, x: Int): Position? {
        for (delta in (1..deltaMax)) {
            val position = state.getOrNull(y - delta)?.getOrNull(x + delta)
            if (position != null && position.isSeat) {
                return position
            }
        }
        return null
    }

    private fun firstNorthWestPosition(deltaMax: Int, y: Int, x: Int): Position? {
        for (delta in (1..deltaMax)) {
            val position = state.getOrNull(y - delta)?.getOrNull(x - delta)
            if (position != null && position.isSeat) {
                return position
            }
        }
        return null
    }

    private fun adjacentPositions(x: Int, y: Int) = listOfNotNull(
        state.getOrNull(y)?.getOrNull(x + 1),
        state.getOrNull(y)?.getOrNull(x - 1),
        state.getOrNull(y + 1)?.getOrNull(x + 1),
        state.getOrNull(y + 1)?.getOrNull(x),
        state.getOrNull(y + 1)?.getOrNull(x - 1),
        state.getOrNull(y - 1)?.getOrNull(x + 1),
        state.getOrNull(y - 1)?.getOrNull(x),
        state.getOrNull(y - 1)?.getOrNull(x - 1),
    )

    private fun occupiedSeatRule(maxOccupied: Int, adjacentPositions: List<Position>): Position {
        return if (adjacentPositions.count { it == Position.Occupied } >= maxOccupied) {
            Position.Empty
        } else {
            Position.Occupied
        }
    }

    private fun emptySeatRule(adjacentPositions: List<Position>): Position {
        return if (adjacentPositions.none { it == Position.Occupied }) {
            Position.Occupied
        } else {
            Position.Empty
        }
    }

    private fun display() {
        println("------------------")
        println(state.joinToString("\n") { row ->
            row.joinToString("") { "${it.symbol}" }
        })
    }

    companion object {
        private const val MAX_ADJACENTS_OCCUPIED_SEATS = 4
    }
}

enum class Position(val symbol: Char, val isSeat: Boolean) {
    Empty('L', true), Occupied('#', true), Floor('.', false);

    companion object {
        private const val EMPTY_SEAT = 'L'
        private const val OCCUPIED_SEAT = '#'
        private const val FLOOR = '.'
        fun from(value: Char): Position {
            return when (value) {
                EMPTY_SEAT -> Empty
                OCCUPIED_SEAT -> Occupied
                FLOOR -> Floor
                else -> error("Invalid position")
            }
        }
    }
}