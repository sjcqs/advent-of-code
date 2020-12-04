package puzzle01

import Puzzle

object Puzzle01Part01 : Puzzle<List<Long>, Long>(Puzzle01Mapper) {
    override val inputFileName: String = "Puzzle01.txt"
    private const val EXPECTED_SUM = 2020L

    override suspend fun doJob(input: List<Long>): Long {
        for (x in input) {
            for (y in input) {
                if (x + y == EXPECTED_SUM) return x * y
            }
        }
        error("Not found")
    }
}

