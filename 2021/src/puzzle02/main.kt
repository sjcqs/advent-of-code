package puzzle02

import readInput
import split

fun main() {
    val example = readInput("example/02.txt").let(::map)
    val input = readInput("02.txt").let(::map)

    assert(part01(example) == 150)
    assert(part02(example) == 900L)

    println(part01(input))
    println(part02(input))

}

fun map(input: String): List<Instruction> {
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

fun part01(input: List<Instruction>): Int {
    val (x, y) = input.fold(0 to 0) { (x, y), instruction ->
        x + instruction.dX to y + instruction.dY
    }
    return x * y
}

fun part02(input: List<Instruction>): Long {
    val (position, depth, _) = input.fold(Triple(0L, 0L, 0L)) { (position, depth, aim), (dX, dY) ->
        val newPosition = position + dX
        val newAim = aim + dY
        val newDepth = depth + (dX * aim)
        Triple(newPosition, newDepth, newAim)
    }
    return position * depth
}

data class Instruction(val dX: Int, val dY: Int) {
    companion object {
        fun forward(value: Int) = Instruction(value, 0)
        fun backward(value: Int) = Instruction(-value, 0)
        fun up(value: Int) = Instruction(0, -value)
        fun down(value: Int) = Instruction(0, value)
    }
}