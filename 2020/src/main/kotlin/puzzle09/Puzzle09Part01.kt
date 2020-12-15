package puzzle09

import InputMapper
import Puzzle

class Puzzle09(private val windowSize: Int) : Puzzle<List<Long>, Puzzle09.Output>(InputMapper {
    it.split("\n").map(String::toLong)
}) {
    data class Output(
        val invalidNumber: Long,
        val weakness: Long
    )

    override val inputFileName = "09.txt"

    override suspend fun doJob(input: List<Long>): Output {
        val invalidNumber = findInvalidNumber(input)
        val contiguousSet = findContiguousSet(invalidNumber, input)
        return Output(invalidNumber, contiguousSet.maxOrNull()!! + contiguousSet.minOrNull()!!)
    }

    private fun findInvalidNumber(input: List<Long>): Long {
        return input.asSequence()
            .windowed(windowSize + 1)
            .first { elements -> !isValid(elements.last(), elements.take(windowSize)) }
            .last()
    }

    private fun findContiguousSet(
        invalidNumber: Long,
        input: List<Long>
    ): List<Long> {
        return input.indices
            .asSequence()
            .flatMap { index ->
                val size = index + 1
                (2..size).flatMap { windowSize -> input.windowed(windowSize) }
            }.first { it.sum() == invalidNumber }
    }

    private fun isValid(result: Long, elements: List<Long>): Boolean {
        return elements.any { a -> elements.any { b -> a + b == result } }
    }
}