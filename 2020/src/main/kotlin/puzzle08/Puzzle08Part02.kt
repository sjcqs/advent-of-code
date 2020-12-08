package puzzle08

import Puzzle

object Puzzle08Part02 : Puzzle<List<Instruction>, Int>(Day08Mapper) {
    override val inputFileName: String = "08.txt"
    private val CONSOLE = GameConsole()

    override suspend fun doJob(input: List<Instruction>): Int {
        val inputs = mutableListOf<List<Instruction>>()
        input.forEachIndexed { index, instruction ->
            if (instruction is Instruction.NoOp) {
                val element = input.toMutableList()
                element[index] = Instruction.Jump(instruction.value)
                inputs.add(element)
            } else if (instruction is Instruction.Jump) {
                val element = input.toMutableList()
                element[index] = Instruction.NoOp(instruction.value)
                inputs.add(element)
            }
        }
        inputs.map { instructions ->
            CONSOLE.run(instructions)
        }.map { result ->
            if (result.status == GameConsole.Status.Finished) {
                return result.accumulator
            }
        }
        return CONSOLE.run(input).accumulator
    }
}