package puzzle08

sealed class Instruction {
    data class NoOp(val value: Int) : Instruction() {
        override fun toString(): String {
            return "NOP $value"
        }
    }
    data class Accumulate(val value: Int) : Instruction() {
        override fun toString(): String {
            return "ACC $value"
        }
    }
    data class Jump(val value: Int) : Instruction() {
        override fun toString(): String {
            return "JMP $value"
        }
    }
}
