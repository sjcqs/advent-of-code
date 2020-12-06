package puzzle06

import Puzzle

object Puzzle06Part02 : Puzzle<List<Group>, Int>(Group.MAPPER) {
    override val inputFileName: String = "06.txt"

    override suspend fun doJob(input: List<Group>): Int {
        return  input.sumOf { it.agreedAnswers }
    }
}