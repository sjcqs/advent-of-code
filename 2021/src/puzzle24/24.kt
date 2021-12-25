package puzzle24

import puzzle22.requireMatchEntire
import readInput
import split

sealed interface Argument {
    companion object {
        fun from(arg: String): Argument {
            val value = arg.toIntOrNull()
            return value?.let(::Value) ?: Variable.from(arg)
        }
    }
}

enum class Variable : Argument {
    W, X, Y, Z;

    companion object {
        fun from(arg: String): Variable {
            return when (arg) {
                "w" -> W
                "x" -> X
                "y" -> Y
                "z" -> Z
                else -> throw IllegalArgumentException("Invalid variable: $arg")
            }
        }
    }
}

data class Value(val value: Int) : Argument

data class Register(
    val w: Int = 0,
    val x: Int = 0,
    val y: Int = 0,
    val z: Int = 0
) {
    operator fun get(variable: Variable) = when (variable) {
        Variable.W -> w
        Variable.X -> x
        Variable.Y -> y
        Variable.Z -> z
    }

    fun update(variable: Variable, value: Int) = when (variable) {
        Variable.W -> copy(w = value)
        Variable.X -> copy(x = value)
        Variable.Y -> copy(y = value)
        Variable.Z -> copy(z = value)
    }
}

/*
inp a
add a b
mul a b
div a b
mod a b
eql a b
*/
sealed interface Instruction {
    fun execute(reg: Register, getInput: () -> Int): Register

    fun Argument.getValue(reg: Register) = when (this) {
        is Value -> value
        is Variable -> reg[this]
    }

    data class Inp(val a: Variable) : Instruction {
        override fun execute(reg: Register, getInput: () -> Int): Register {
            val value = getInput()
            return reg.update(a, value)
        }
    }

    data class Add(val a: Variable, val b: Argument) : Instruction {
        override fun execute(reg: Register, getInput: () -> Int): Register {
            return reg.update(a, reg[a] + b.getValue(reg))
        }
    }

    data class Mul(val a: Variable, val b: Argument) : Instruction {
        override fun execute(reg: Register, getInput: () -> Int): Register {
            return reg.update(a, reg[a] * b.getValue(reg))
        }
    }

    data class Div(val a: Variable, val b: Argument) : Instruction {
        override fun execute(reg: Register, getInput: () -> Int): Register {
            return reg.update(a, reg[a] / b.getValue(reg))
        }
    }

    data class Mod(val a: Variable, val b: Argument) : Instruction {
        override fun execute(reg: Register, getInput: () -> Int): Register {
            return reg.update(a, reg[a].rem(b.getValue(reg)))
        }
    }

    data class Eql(val a: Variable, val b: Argument) : Instruction {
        override fun execute(reg: Register, getInput: () -> Int): Register {
            return reg.update(a, if (reg[a] == b.getValue(reg)) 1 else 0)
        }
    }
}

/*
inp a
add a b
mul a b
div a b
mod a b
eql a b
*/
val inpRegex = """inp ([wxyz])""".toRegex()
val addRegex = """add ([wxyz]) ([wxyz]|[0-9-]+)""".toRegex()
val mulRegex = """mul ([wxyz]) ([wxyz]|[0-9-]+)""".toRegex()
val divRegex = """div ([wxyz]) ([wxyz]|[0-9-]+)""".toRegex()
val modRegex = """mod ([wxyz]) ([wxyz]|[0-9-]+)""".toRegex()
val eqlRegex = """eql ([wxyz]) ([wxyz]|[0-9-]+)""".toRegex()
fun map(input: String): List<Instruction> = input.split().map { instruction ->
    when {
        inpRegex.matches(instruction) -> {
            val (a) = inpRegex.requireMatchEntire(instruction).destructured
            Instruction.Inp(Variable.from(a))
        }
        addRegex.matches(instruction) -> {
            val (a, b) = addRegex.requireMatchEntire(instruction).destructured
            Instruction.Add(Variable.from(a), Argument.from(b))
        }
        mulRegex.matches(instruction) -> {
            val (a, b) = mulRegex.requireMatchEntire(instruction).destructured
            Instruction.Mul(Variable.from(a), Argument.from(b))
        }
        divRegex.matches(instruction) -> {
            val (a, b) = divRegex.requireMatchEntire(instruction).destructured
            Instruction.Div(Variable.from(a), Argument.from(b))
        }
        modRegex.matches(instruction) -> {
            val (a, b) = modRegex.requireMatchEntire(instruction).destructured
            Instruction.Mod(Variable.from(a), Argument.from(b))
        }
        eqlRegex.matches(instruction) -> {
            val (a, b) = eqlRegex.requireMatchEntire(instruction).destructured
            Instruction.Eql(Variable.from(a), Argument.from(b))
        }
        else -> error("Invalid instruction: $instruction")
    }
}

fun List<Instruction>.execute(reg: Register = Register(), getInput: () -> Int): Register {
    return foldIndexed(reg) { index, currentReg, instruction ->
        println("Instruction $index / $size")
        instruction.execute(currentReg, getInput)
    }
}

fun part01(instructions: List<Instruction>): Long {
    return (11_111_111_111_111L..99_999_999_999_999L)
        .asSequence()
        .filter { !it.toString().contains("0") }
        .maxOrNull() ?: 0L
}

fun main() {
    /*listOf("24-1.txt", "24-2.txt", "24-3.txt").forEach { fileName ->
        val instructions = map(readInput("example/$fileName"))
        println("$fileName: ${instructions.execute(getInput = { 1 })}")
    }*/

    val input = map(readInput("24.txt"))
    println(run(false))
    println(run(true))
}

fun run(part2: Boolean): Any {
    val blocks = readInput("24.txt").split().chunked(18)
    val result = MutableList(14) { -1 }
    val buffer = ArrayDeque<Pair<Int, Int>>()
    fun List<String>.lastOf(command: String) = last { it.startsWith(command) }.split(" ").last().toInt()
    val best = if (part2) 1 else 9
    blocks.forEachIndexed { index, instructions ->
        if ("div z 26" in instructions) {
            val offset = instructions.lastOf("add x")
            val (lastIndex, lastOffset) = buffer.removeFirst()
            val difference = offset + lastOffset
            if (difference >= 0) {
                result[lastIndex] = if (part2) best else best - difference
                result[index] = if (part2) best + difference else best
            } else {
                result[lastIndex] = if (part2) best - difference else best
                result[index] = if (part2) best else best + difference
            }
        } else buffer.addFirst(index to instructions.lastOf("add y"))
    }

    return result.joinToString("").toLong()
}