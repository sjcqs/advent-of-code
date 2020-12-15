package puzzle15

import InputMapper
import Puzzle

class Puzzle15(private val turns: Int) : Puzzle<Puzzle15.Input, Puzzle15.Output>(
    InputMapper { input ->
        val numbers = input.split(",")
            .map(String::toInt)
        Input(numbers)
    }
) {

    data class Input(
        val numbers: List<Int>
    )

    data class Output(
        val lastNumber: Int
    )

    override val inputFileName: String = "15.txt"

    override suspend fun doJob(input: Input): Output {
        val indices = hashMapOf<Int, Occurrence>()
        input.numbers.forEachIndexed { index, number -> indices[number] = Occurrence(index + 1) }
        log(input.numbers.joinToString("\n"))
        var previousNumber = input.numbers.last()
        repeat(turns - input.numbers.size) { index ->
            val turn = index + input.numbers.size + 1
            previousNumber = indices[previousNumber]?.diff() ?: 0
            indices.compute(previousNumber) { _, occurrence ->
                occurrence?.occurred(turn) ?: Occurrence(turn)
            }
            log("[$turn] $previousNumber")
        }
        return Output(previousNumber)
    }

    data class Occurrence(val turn: Int, val previousTurn: Int? = null) {
        fun occurred(turn: Int): Occurrence = copy(turn = turn, previousTurn = this.turn)
        fun diff() = previousTurn?.let { turn - previousTurn } ?: 0
    }
}