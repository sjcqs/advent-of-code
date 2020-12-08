package puzzle08

import Puzzle

object Puzzle08Part01 : Puzzle<List<Instruction>, Int>(Day08Mapper) {
    override val inputFileName: String = "08.txt"
    private val CONSOLE = GameConsole()

    override suspend fun doJob(input: List<Instruction>): Int {
        return CONSOLE.run(input).accumulator
    }
}