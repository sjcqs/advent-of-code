package puzzle01

import Puzzle

object Puzzle01Part02 : Puzzle<List<Long>, Int>(Puzzle01Mapper) {
    override val inputFileName: String = "01.txt"

    override suspend fun doJob(input: List<Long>): Int {
        return input.asSequence()
            .windowed(3)
            .filter { window -> window.size == 3 }
            .map { window -> window.sum() }
            .windowed(2)
            .count { (first, second) -> second > first }
    }
}