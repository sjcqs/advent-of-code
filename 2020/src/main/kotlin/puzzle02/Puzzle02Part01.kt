package puzzle02

import Puzzle
import split

object Puzzle02Part01 : Puzzle<List<Entry>, Int>(Puzzle02Mapper) {
    override val inputFileName: String = "Puzzle02.txt"

    override suspend fun doJob(input: List<Entry>): Int {
        return input.count(Entry::isValidAccordingToCount)
    }
}

