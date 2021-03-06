package computer

class IoComputer(
    val id: Long,
    var input: MutableList<Long>,
    var output: MutableList<Long> = mutableListOf()
) {

    var isFinished = false

    fun run(args: LongArray) {
        var isRunning = true
        var index = 0
        while (isRunning) {
            if (index >= args.size) {
                isRunning = false
            } else {
                println("[$id] Running at $index")
                val params = args[index]
                val (mode0, mode1, mode2, opcode) = parseParams(params.toInt())
                when (opcode) {
                    Opcode.MUL -> {
                        val input0 = input(mode0, args, index + 1)
                        val input1 = input(mode1, args, index + 2)
                        val input2 = input(Mode.IMMEDIATE, args, index + 3)
                        multiply(input0, input1, input2.toInt(), args)
                        index += 4
                    }
                    Opcode.SUM -> {
                        val input0 = input(mode0, args, index + 1)
                        val input1 = input(mode1, args, index + 2)
                        val input2 = input(Mode.IMMEDIATE, args, index + 3)
                        sum(input0, input1, input2.toInt(), args)
                        index += 4
                    }
                    Opcode.SET -> {
                        val input0 = input(Mode.IMMEDIATE, args, index + 1)
                        readInput(input0.toInt(), args)
                        index += 2
                    }
                    Opcode.DISPLAY -> {
                        val input0 = input(mode0, args, index + 1)
                        display(input0)
                        index += 2
                    }
                    Opcode.JMP_TRUE -> {
                        val input0 = input(mode0, args, index + 1)
                        val input1 = input(mode1, args, index + 2)
                        val doJump = jump(input0 != 0L, input1, index, args)
                        index = if (doJump) args[index].toInt() else index + 3
                    }
                    Opcode.JMP_FALSE -> {
                        val input0 = input(mode0, args, index + 1)
                        val input1 = input(mode1, args, index + 2)
                        val doJump = jump(input0 == 0L, input1, index, args)
                        index = if (doJump) args[index].toInt() else index + 3
                    }
                    Opcode.LT -> {
                        val input0 = input(mode0, args, index + 1)
                        val input1 = input(mode1, args, index + 2)
                        val input2 = input(Mode.IMMEDIATE, args, index + 3)
                        lessThan(input0, input1, input2.toInt(), args)
                        index += 4
                    }
                    Opcode.EQ -> {
                        val input0 = input(mode0, args, index + 1)
                        val input1 = input(mode1, args, index + 2)
                        val input2 = input(Mode.IMMEDIATE, args, index + 3)
                        eq(input0, input1, input2.toInt(), args)
                        index += 4
                    }
                    Opcode.EOF -> {
                        isRunning = false
                    }
                }
            }
        }
        println("[$id] Finished")
        isFinished = true
    }

    private fun input(mode0: Mode, args: LongArray, address: Int): Long {
        val value = args[address]
        return when (mode0) {
            Mode.IMMEDIATE -> value.also { println("[$id] Immediate: $it") }
            Mode.POSITION -> args[value.toInt()].also { println("[$id] Position at $value: $it") }
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

        println("[$id] Parse params: $params")

        return Params(
            mode0 = Mode.from(mode0),
            mode1 = Mode.from(mode1),
            mode2 = Mode.from(mode2),
            opcode = Opcode.from(opcode)
        )
    }

    private fun display(value: Long) {
        println("[$id] Display: $value")
        synchronized(output) {
            output.add(value)
        }
    }

    private fun readInput(address: Int, args: LongArray) {
        while (input.isEmpty()) {
            Thread.sleep(10)
            println("[$id] Waiting for input to write at $address")
        }
        val value = synchronized(input) { input.removeAt(0) }
        println("[$id] Write input: $value to $address")
        args[address] = value
    }

    private fun sum(input0: Long, input1: Long, address: Int, args: LongArray) {
        println("[$id] Sum $input0 + $input1 at $address")
        args[address] = input0 + input1
    }

    private fun multiply(input0: Long, input1: Long, address: Int, args: LongArray) {
        println("[$id] Multiply $input0 * $input1 at $address")
        args[address] = input0 * input1
    }

    private fun eq(input0: Long, input1: Long, input2: Int, args: LongArray) {
        println("[$id] Eq $input0 == $input0")
        args[input2] = if (input0 == input1) 1 else 0
    }

    private fun lessThan(input0: Long, input1: Long, input2: Int, args: LongArray) {
        println("[$id] Eq $input0 < $input0")
        args[input2] = if (input0 < input1) 1 else 0
    }

    private fun jump(doJump: Boolean, input1: Long, index: Int, args: LongArray): Boolean {
        println("[$id] Jump at $input1: $doJump ($index)")
        if (doJump) {
            args[index] = input1
        }
        return doJump
    }

    companion object {
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
    }

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
}