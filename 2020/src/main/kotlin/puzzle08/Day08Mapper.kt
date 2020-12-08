package puzzle08

import InputMapper
import split

object Day08Mapper : InputMapper<List<Instruction>> {
    private const val ID_ACCUMULATE = "acc"
    private const val ID_JUMP = "jmp"
    private const val ID_NO_OP = "nop"
    override fun map(input: String): List<Instruction> {
        return input.split().map { it.toInstruction() }
    }

    private fun String.toInstruction(): Instruction {
        val (id, value) = split(" ")
        return when (id) {
            ID_ACCUMULATE -> Instruction.Accumulate(value.toInt())
            ID_JUMP -> Instruction.Jump(value.toInt())
            ID_NO_OP -> Instruction.NoOp(value.toInt())
            else -> error("Invalid instruction.")
        }
    }

}