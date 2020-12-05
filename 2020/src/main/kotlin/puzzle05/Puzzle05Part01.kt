package puzzle05

import Puzzle

object Puzzle05Part01 : Puzzle<List<Seat>, Int>(mapper) {
    override val inputFileName: String = "05.txt"

    override suspend fun doJob(input: List<Seat>): Int {
        return input.maxByOrNull { it.id }
            ?.id ?: -1
    }
}