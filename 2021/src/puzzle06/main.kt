package puzzle06

import readInput

fun main() {
    val example = readInput("example/06.txt").let(::map)
    val input = readInput("06.txt").let(::map)

    assert(simulate(example, 18) == 26L) { "Part01 (18): expected 26, got ${simulate(example, 18)}" }
    assert(simulate(example) == 5934L) { "Part01 (80): expected 5934, got ${simulate(example)}" }
    assert(simulate(example, 256) == 26984457539L) { "Part02: expected 5934, got ${simulate(example)}" }

    println("Part01: ${simulate(input)}")
    println("Part02: ${simulate(input, 256)}")
}

private fun simulate(input: List<Int>, days: Int = 80): Long {
    val fishesDemography = input.groupingBy { age -> age }
        .eachCount()
        .mapValues { it.value.toLong() }
        .toMutableMap()
    repeat(days) {
        val aged7 = fishesDemography.getOrDefault(7, 0L)
        val aged0 = fishesDemography.getOrDefault(0, 0L)

        for (age in 0..8) {
            when (age) {
                6 -> fishesDemography[6] = aged7 + aged0
                8 -> fishesDemography[8] = aged0
                else -> fishesDemography[age] = fishesDemography.getOrDefault(age + 1, 0L)
            }
        }

    }
    return fishesDemography.values.sum()
}

private fun map(input: String): List<Int> {
    return input.split(",").map(String::toInt)
}