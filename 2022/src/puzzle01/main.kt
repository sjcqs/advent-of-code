package puzzle01

import readInput

fun main() {
    val example = readInput("example/01.txt").let(::map)
    val input = readInput("01.txt").let(::map)

    require(part01(example) == 24000L)
    require(part02(example) == 45000L)

    println(part01(input))
    println(part02(input))

}

fun map(input: String): List<List<Long>> {
    return input.split("\n\n").map { reserve ->
        reserve.split("\n").mapNotNull {
            it.toLongOrNull()
        }
    }
}

fun part01(input: List<List<Long>>): Long {
    return input.maxOf { it.sum() }

}

fun part02(input: List<List<Long>>): Long {
    return input.sortedByDescending { it.sum() }
        .take(3)
        .flatten()
        .sum()
}