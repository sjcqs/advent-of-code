package puzzle01

import Puzzle

object Puzzle01Part02 : Puzzle<List<Long>, Long>(Puzzle01Mapper) {
    override val inputFileName: String = "Puzzle01.txt"
    private const val EXPECTED_SUM = 2020L

    override suspend fun doJob(input: List<Long>): Long {
        for (x in input) {
            for (y in input) {
                for (z in input) {
                    if (x + y + z == EXPECTED_SUM) return x * y * z
                }
            }
        }
        error("Not found")
    }
}