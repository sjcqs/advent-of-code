package puzzle01

import InputMapper
import split

object Puzzle01Mapper : InputMapper<List<Long>> {
    override fun map(input: String): List<Long> {
        return input.split().map(String::toLong)
    }
}