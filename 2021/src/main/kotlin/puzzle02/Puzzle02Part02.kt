package puzzle02

import Puzzle

object Puzzle02Part02 : Puzzle<List<Instruction>, Long>(Puzzle02Mapper) {
    override val inputFileName: String = "02.txt"

    override suspend fun doJob(input: List<Instruction>): Long {
        val (position, depth, _) = input.fold(Triple(0L, 0L, 0L)) { (position, depth, aim), (dX, dY) ->
            val newPosition = position + dX
            val newAim = aim + dY
            val newDepth = depth + (dX * aim)
            Triple(newPosition, newDepth, newAim)
        }
        return position * depth
    }
}