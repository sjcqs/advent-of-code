package days

object J005 {
    const val INPUT =
        "3,225,1,225,6,6,1100,1,238,225,104,0,1102,79,14,225,1101,17,42,225,2,74,69,224,1001,224,-5733,224,4,224,1002,223,8,223,101,4,224,224,1,223,224,223,1002,191,83,224,1001,224,-2407,224,4,224,102,8,223,223,101,2,224,224,1,223,224,223,1101,18,64,225,1102,63,22,225,1101,31,91,225,1001,65,26,224,101,-44,224,224,4,224,102,8,223,223,101,3,224,224,1,224,223,223,101,78,13,224,101,-157,224,224,4,224,1002,223,8,223,1001,224,3,224,1,224,223,223,102,87,187,224,101,-4698,224,224,4,224,102,8,223,223,1001,224,4,224,1,223,224,223,1102,79,85,224,101,-6715,224,224,4,224,1002,223,8,223,1001,224,2,224,1,224,223,223,1101,43,46,224,101,-89,224,224,4,224,1002,223,8,223,101,1,224,224,1,223,224,223,1101,54,12,225,1102,29,54,225,1,17,217,224,101,-37,224,224,4,224,102,8,223,223,1001,224,3,224,1,223,224,223,1102,20,53,225,4,223,99,0,0,0,677,0,0,0,0,0,0,0,0,0,0,0,1105,0,99999,1105,227,247,1105,1,99999,1005,227,99999,1005,0,256,1105,1,99999,1106,227,99999,1106,0,265,1105,1,99999,1006,0,99999,1006,227,274,1105,1,99999,1105,1,280,1105,1,99999,1,225,225,225,1101,294,0,0,105,1,0,1105,1,99999,1106,0,300,1105,1,99999,1,225,225,225,1101,314,0,0,106,0,0,1105,1,99999,107,226,226,224,1002,223,2,223,1006,224,329,101,1,223,223,1108,677,226,224,1002,223,2,223,1006,224,344,101,1,223,223,7,677,226,224,102,2,223,223,1006,224,359,101,1,223,223,108,226,226,224,1002,223,2,223,1005,224,374,101,1,223,223,8,226,677,224,1002,223,2,223,1006,224,389,101,1,223,223,1108,226,226,224,102,2,223,223,1006,224,404,101,1,223,223,1007,677,677,224,1002,223,2,223,1006,224,419,101,1,223,223,8,677,677,224,1002,223,2,223,1005,224,434,1001,223,1,223,1008,226,226,224,102,2,223,223,1005,224,449,1001,223,1,223,1008,226,677,224,102,2,223,223,1006,224,464,101,1,223,223,1107,677,677,224,102,2,223,223,1006,224,479,101,1,223,223,107,677,677,224,1002,223,2,223,1005,224,494,1001,223,1,223,1107,226,677,224,1002,223,2,223,1005,224,509,101,1,223,223,1108,226,677,224,102,2,223,223,1006,224,524,101,1,223,223,7,226,226,224,1002,223,2,223,1005,224,539,101,1,223,223,108,677,677,224,1002,223,2,223,1005,224,554,101,1,223,223,8,677,226,224,1002,223,2,223,1005,224,569,1001,223,1,223,1008,677,677,224,102,2,223,223,1006,224,584,101,1,223,223,107,226,677,224,102,2,223,223,1005,224,599,1001,223,1,223,7,226,677,224,102,2,223,223,1005,224,614,101,1,223,223,1007,226,226,224,1002,223,2,223,1005,224,629,101,1,223,223,1107,677,226,224,1002,223,2,223,1006,224,644,101,1,223,223,108,226,677,224,102,2,223,223,1006,224,659,101,1,223,223,1007,677,226,224,102,2,223,223,1006,224,674,101,1,223,223,4,223,99,226"
    private const val EOF_VALUE = 99
    private const val SUM_VALUE = 1
    private const val MUL_VALUE = 2
    private const val SET_VALUE = 3
    private const val DISPLAY_VALUE = 4
    private const val JMP_TRUE_VALUE = 5
    private const val JMP_FALSE_VALUE = 6
    private const val LT_VALUE = 7
    private const val EQ_VALUE = 8
    private const val IMODE = 1
    private const val PMODE = 0

    enum class Mode {
        IMMEDIATE, POSITION;

        companion object {
            fun from(value: Int): Mode = when (value) {
                IMODE -> IMMEDIATE
                PMODE -> POSITION
                else -> throw IllegalArgumentException("Unknown mode $value")
            }
        }
    }

    enum class Opcode {
        EOF,
        SUM,
        MUL,
        SET,
        JMP_TRUE,
        JMP_FALSE,
        LT,
        EQ,
        DISPLAY;

        companion object {
            fun from(value: Int): Opcode = when (value) {
                EOF_VALUE -> EOF
                SUM_VALUE -> SUM
                MUL_VALUE -> MUL
                SET_VALUE -> SET
                DISPLAY_VALUE -> DISPLAY
                JMP_TRUE_VALUE -> JMP_TRUE
                JMP_FALSE_VALUE -> JMP_FALSE
                LT_VALUE -> LT
                EQ_VALUE -> EQ
                else -> throw IllegalArgumentException("Unknown opcode $value")
            }
        }
    }


    fun run(args: IntArray, input: Int): List<String> {
        val console = mutableListOf<String>()
        var isRunning = true
        var index = 0
        while (isRunning) {
            println("Index: $index;\n${args.joinToString()};\nConsole: ${console.joinToString()}")
            if (index >= args.size) {
                throw IllegalStateException("Index out of bound. $index > ${args.size}")
            }
            val params = args[index]
            println(args.copyOfRange(index, index+4).joinToString())
            val (mode0, mode1, mode2, opcode) = parseParams(params)
            when (opcode) {
                Opcode.MUL -> {
                    val input0 = input(mode0, args, index + 1)
                    val input1 = input(mode1, args, index + 2)
                    val input2 = input(Mode.IMMEDIATE, args, index + 3)
                    multiply(input0, input1, input2, args)
                    index += 4
                }
                Opcode.SUM -> {
                    val input0 = input(mode0, args, index + 1)
                    val input1 = input(mode1, args, index + 2)
                    val input2 = input(Mode.IMMEDIATE, args, index + 3)
                    sum(input0, input1, input2, args)
                    index += 4
                }
                Opcode.SET -> {
                    val input0 = input(Mode.IMMEDIATE, args, index + 1)
                    setValue(input, input0, args)
                    index += 2
                }
                Opcode.DISPLAY -> {
                    val input0 = input(mode0, args, index + 1)
                    display(input0, console)
                    index += 2
                }
                Opcode.JMP_TRUE -> {
                    val input0 = input(mode0, args, index + 1)
                    val input1 = input(mode1, args, index + 2)
                    val doJump = jump(input0 != 0, input1, index, args)
                    index = if (doJump) args[index] else index + 3
                }
                Opcode.JMP_FALSE -> {
                    val input0 = input(mode0, args, index + 1)
                    val input1 = input(mode1, args, index + 2)
                    val doJump = jump(input0 == 0, input1, index, args)
                    index = if (doJump) args[index] else index + 3
                }
                Opcode.LT -> {
                    val input0 = input(mode0, args, index + 1)
                    val input1 = input(mode1, args, index + 2)
                    val input2 = input(Mode.IMMEDIATE, args, index + 3)
                    lessThan(input0, input1, input2, args)
                    index += 4
                }
                Opcode.EQ -> {
                    val input0 = input(mode0, args, index + 1)
                    val input1 = input(mode1, args, index + 2)
                    val input2 = input(Mode.IMMEDIATE, args, index + 3)
                    eq(input0, input1, input2, args)
                    index += 4
                }
                Opcode.EOF -> isRunning = false
            }
            println()
        }
        return console
    }

    private fun input(mode0: Mode, args: IntArray, address: Int): Int {
        val value = args[address]
        return when (mode0) {
            Mode.IMMEDIATE -> value
            Mode.POSITION -> args[value]
        }
    }

    data class Params(
        val mode0: Mode,
        val mode1: Mode,
        val mode2: Mode,
        val opcode: Opcode
    )

    private fun parseParams(params: Int): Params {
        val opcode = params % 100
        val mode0 = (params / 100) % 10
        val mode1 = (params / 1000) % 10
        val mode2 = (params / 10000) % 10

        println("Parse params: $params")

        return Params(
            mode0 = Mode.from(mode0),
            mode1 = Mode.from(mode1),
            mode2 = Mode.from(mode2),
            opcode = Opcode.from(opcode)
        ).also {
            println(it)
        }
    }

    private fun display(value: Int, console: MutableList<String>) {
        println("Display: $value")
        console.add(value.toString())
    }

    private fun setValue(input: Int, address: Int, args: IntArray) {
        println("Set value: $input at $address")
        args[address] = input
    }

    private fun sum(input0: Int, input1: Int, address: Int, args: IntArray) {
        println("Sum $input0 + $input1 at $address")
        args[address] = input0 + input1
    }

    private fun multiply(input0: Int, input1: Int, address: Int, args: IntArray) {
        println("Multiply $input0 + $input1 at $address")
        args[address] = input0 * input1
    }

    private fun eq(input0: Int, input1: Int, input2: Int, args: IntArray) {
        println("Eq $input0 == $input0")
        args[input2] = if (input0 == input1) 1 else 0
    }

    private fun lessThan(input0: Int, input1: Int, input2: Int, args: IntArray) {
        println("Eq $input0 < $input0")
        args[input2] = if (input0 < input1) 1 else 0
    }

    private fun jump(doJump: Boolean, input1: Int, index: Int, args: IntArray): Boolean {
        println("Jump at $input1: $doJump ($index)")
        if (doJump) {
            args[index] = input1
        }
        return doJump
    }
}