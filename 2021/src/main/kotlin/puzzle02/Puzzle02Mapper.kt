package puzzle02

import InputMapper
import split

object Puzzle02Mapper : InputMapper<List<Instruction>> {
    override fun map(input: String): List<Instruction> {
        return input.split().mapNotNull { line ->
            val (type, value) = line.split(" ")
            when (type) {
                "forward" -> Instruction.forward(value.toInt())
                "backward" -> Instruction.backward(value.toInt())
                "up" -> Instruction.up(value.toInt())
                "down" -> Instruction.down(value.toInt())
                else -> null
            }
        }
    }
}

data class Instruction(val dX: Int, val dY: Int) {
    companion object {
        fun forward(value: Int) = Instruction(value, 0)
        fun backward(value: Int) = Instruction(-value, 0)
        fun up(value: Int) = Instruction(0, -value)
        fun down(value: Int) = Instruction(0, value)
    }
}