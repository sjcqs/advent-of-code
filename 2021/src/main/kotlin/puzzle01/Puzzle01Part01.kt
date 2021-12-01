package puzzle01

import Puzzle

object Puzzle01Part01 : Puzzle<List<Long>, Int>(Puzzle01Mapper) {
    override val inputFileName: String = "01.txt"

    override suspend fun doJob(input: List<Long>): Int {
        return input.windowed(2)
            .count { (first, second) -> second > first }
    }
}

