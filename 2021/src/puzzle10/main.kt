package puzzle10

import readInput
import split


sealed interface Result {
    object Valid : Result
    data class Incomplete(val missing: String) : Result
    data class Invalid(val illegalBoundary: Char) : Result
}

fun main() {
    val boundaries = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>',
    )
    val invalidScores = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137,
    )
    val incompleteScores = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4,
    )

    fun validate(line: String): Result {
        if (line.isEmpty()) return Result.Valid
        if (line.first() !in boundaries.keys) {
            return Result.Invalid(line.first())
        }
        val queue = line.drop(1).fold(mutableListOf(line.first())) { queue, c ->
            val current = queue.lastOrNull()
            when (c) {
                in boundaries.keys -> queue.add(c)
                in boundaries.values -> {
                    if (current == null || c != boundaries[current]) {
                        return Result.Invalid(c)
                    } else if (c == boundaries[current]) {
                        queue.removeLastOrNull()
                    }
                }
                else -> return Result.Invalid(c)
            }
            queue
        }
        return if (queue.isEmpty()) {
            Result.Valid
        } else {
            Result.Incomplete(queue.foldRight("") { c, missing ->
                missing + boundaries.getValue(c)
            })
        }
    }

    fun part01(lines: List<String>): Int {
        return lines.associateWith { line -> validate(line) }
            .map { (_, result) ->
                when (result) {
                    is Result.Valid -> 0
                    is Result.Incomplete -> 0
                    is Result.Invalid -> invalidScores[result.illegalBoundary] ?: 0
                }
            }.sum()
    }

    fun part02(lines: List<String>): Long {
        return lines.associateWith { line -> validate(line) }
            .filterValues { it is Result.Incomplete }
            .entries
            .map { (_, result) ->
                if (result is Result.Incomplete) {
                    result.missing.fold(0L) { score, c ->
                        (score * 5) + incompleteScores.getValue(c)
                    }
                } else {
                    0
                }
            }.sorted()
            .let { it[(it.lastIndex / 2)] }
    }

    val example = readInput("example/10.txt").split()
    val input = readInput("10.txt").split()

    assert(part01(example) == 26397) { "part01(example) should be 26397, got ${part01(example)}" }
    println(part01(input))

    assert(part02(example) == 288957L) { "part02(example) should be 288957, got ${part02(example)}" }
    println(part02(input))
}