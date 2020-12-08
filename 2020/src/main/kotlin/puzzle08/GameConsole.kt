package puzzle08

class GameConsole {
    var accumulator = 0
    var cursor = 0

    fun run(
        instructions: List<Instruction>,
        initialCursor: Int = 0,
        initialAccumulator: Int = 0
    ): Result {
        val encountered = mutableMapOf<Int, Boolean>()

        accumulator = initialAccumulator
        cursor = initialCursor

        var instruction = instructions[cursor]

        while (cursor in instructions.indices) {
            encountered[cursor] = true
            onInstruction(instruction)
            if (isFinished(instructions.size)) {
                return Result(accumulator, Status.Finished)
            } else {
                instruction = instructions[cursor]
                if (encountered.getOrDefault(cursor, false)) {
                    return Result(accumulator, Status.Aborted)
                }
            }
        }

        return Result(accumulator, Status.Finished)
    }

    private fun onInstruction(instruction: Instruction) {
        val (dCursor, dAccumulator) = when (instruction) {
            is Instruction.NoOp -> 1 to 0
            is Instruction.Accumulate -> 1 to instruction.value
            is Instruction.Jump -> instruction.value to 0
        }
        cursor += dCursor
        accumulator += dAccumulator
    }

    private fun isFinished(size: Int) = cursor == size

    enum class Status { Aborted, Finished }
    data class Result(val accumulator: Int, val status: Status)
}