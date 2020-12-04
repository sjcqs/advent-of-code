package puzzle04

import Puzzle

object Puzzle04Part01 : Puzzle<List<Passport>, Int>(Puzzle04Mapper) {
    override val inputFileName: String = "Puzzle04.txt"

    override suspend fun doJob(input: List<Passport>): Int {
        return input.count(Passport::containAllRequiredFields)
    }
}

