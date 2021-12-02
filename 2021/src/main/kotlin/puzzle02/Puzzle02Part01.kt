package puzzle02

import Puzzle

object Puzzle02Part01 : Puzzle<List<Instruction>, Int>(Puzzle02Mapper) {
    override var inputFileName: String = "02.txt"

    override suspend fun doJob(input: List<Instruction>): Int {
        val (x, y) = input.fold(0 to 0) { (x, y), instruction ->
            x + instruction.dX to y + instruction.dY
        }
        return x * y
    }
}

