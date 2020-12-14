package puzzle14

import InputMapper
import Puzzle

object Puzzle14 : Puzzle<Puzzle14.Input, Puzzle14.Output>(
    InputMapper { input ->
        val instructions = input.split("\n")
            .mapNotNull(Instruction.Companion::fromString)
        Input(instructions)
    }
) {
    override val inputFileName: String = "14.txt"

    override suspend fun doJob(input: Input): Output {
        return Output(
            Computer().run(input.instructions).sum(),
            Computer().run(input.instructions, Computer.Mode.MemoryAccessDecoder).sum()
        )
    }

    data class Input(val instructions: List<Instruction>)

    data class Output(
        val memorySum: Long,
        val memorySumAddressDecoded: Long
    )
}


class Computer(
    private val memory: MutableMap<Long, Long> = mutableMapOf(),
    private var mask: Instruction.Mask = Instruction.Mask("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
) {

    fun sum() = memory.values.sum()
    fun run(instructions: List<Instruction>, mode: Mode = Mode.BitMask): Computer {
        instructions.forEach {
            run(it, mode)
        }
        return this
    }

    private fun run(instruction: Instruction, mode: Mode) {
        when (instruction) {
            is Instruction.Mask -> mask = instruction
            is Instruction.Write -> {
                when (mode) {
                    Mode.MemoryAccessDecoder -> {
                        writeToAddresses(instruction)
                    }
                    Mode.BitMask -> {
                        writeMasked(instruction)
                    }
                }
            }
        }
    }

    private fun writeToAddresses(instruction: Instruction.Write) {
        val address = instruction.address
        val binaryAddress = address.toBinaryString(mask.length)

        val maskedAddress = mask.mask.mapIndexed { index, bitMask ->
            when (bitMask) {
                '1' -> '1'
                'X' -> 'X'
                else -> binaryAddress[index]
            }
        }
        val addressStrings = maskedAddress.foldRightIndexed(listOf(maskedAddress)) { bitIndex, c, addresses ->
            if (c == 'X') {
                val generatedAddresses = addresses.flatMap {
                    listOf(
                        it.mapIndexed { index, c -> if (index == bitIndex) '0' else c },
                        it.mapIndexed { index, c -> if (index == bitIndex) '1' else c }
                    )
                }
                addresses.filterNot { it[bitIndex] == 'X' } + generatedAddresses
            } else {
                addresses
            }
        }
        addressStrings.map { it.toCharArray().concatToString().toLong(2) }
            .onEach { writeAddress -> memory[writeAddress] = instruction.value }
    }

    private fun writeMasked(instruction: Instruction.Write) {
        val masked = mask.mask(instruction.value)
        memory[instruction.address] = masked
    }

    enum class Mode {
        MemoryAccessDecoder, BitMask
    }

}

sealed class Instruction {
    data class Mask(val mask: String) : Instruction() {
        val length = mask.length
        fun mask(value: Long): Long {
            val binaryValue = value.toBinaryString(mask.length)
            val masked = mask.mapIndexed { index, bitMask ->
                when (bitMask) {
                    '0' -> '0'
                    '1' -> '1'
                    else -> binaryValue.getOrElse(index) { '0' }
                }
            }.toCharArray().concatToString()

            return masked.toLong(2)
        }
    }

    data class Write(val address: Long, val value: Long) : Instruction()

    companion object {
        private val MEM_REGEX = """mem\[(\d+)] = (\d+)""".toRegex()
        private val MASK_REGEX = """mask = ([X01]+)""".toRegex()
        fun fromString(line: String): Instruction? {
            return when {
                line.startsWith("mem") -> MEM_REGEX.matchEntire(line)?.let { result ->
                    val (index, value) = result.destructured
                    Write(index.toLong(), value.toLong())
                }
                line.startsWith("mask") -> MASK_REGEX.matchEntire(line)?.let { result ->
                    val (mask) = result.destructured
                    return Mask(mask)
                }
                else -> null
            }
        }
    }
}

fun Long.toBinaryString(length: Int): String {
    var text = toString(2)
    repeat(length - text.length) {
        text = "0$text"
    }
    return text
}

fun Int.toBinaryString(length: Int): String {
    var text = toString(2)
    repeat(length - text.length) {
        text = "0$text"
    }
    return text
}