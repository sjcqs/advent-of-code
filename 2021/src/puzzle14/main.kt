package puzzle14

import readInput
import split


fun main() {
    data class Input(
        val template: String,
        val rules: Map<Pair<Char, Char>, Char>
    )

    fun part01(input: Input, steps: Int): Long {
        val template = input.template
        val initial = input.template.windowed(2)
            .groupingBy { it }
            .eachCount()
            .mapValues { (_, count) -> count.toLong() }
            .mapKeys { (key, _) -> key[0] to key[1] }
        var pairFrequencies = initial
        repeat(steps) {
            val previous = pairFrequencies
            pairFrequencies = buildMap {
                previous.forEach { (pair, count) ->
                    val (left, right) = pair
                    val inserted = input.rules[pair]

                    if (inserted != null) {
                        put(left to inserted, getOrDefault(left to inserted, 0L) + count)
                        put(inserted to right, getOrDefault(inserted to right, 0L) + count)
                    } else {
                        put(pair, count)
                    }
                }
            }
        }
        val frequencies = buildMap<Char, Long> {
            put(template[0], 1L)
            pairFrequencies.forEach { (pair, count) ->
                put(pair.second, getOrDefault(pair.second, 0L) + count)
            }
        }
        val min = frequencies.minOf { (_, count) -> count }
        val max = frequencies.maxOf { (_, count) -> count }
        return max - min
    }


    fun map(input: String): Input {
        val (template, rules) = input.split("\n\n")

        return Input(
            template = template,
            rules = rules.split().associate {
                val (left, right) = it.split(" -> ")
                Pair(left[0], left[1]) to right[0]
            }
        )
    }

    val input = readInput("14.txt").let(::map)
    val example = readInput("example/14.txt").let(::map)

    assert(part01(example, 10) == 1588L) { "expected 1588, got ${part01(example, 10)}" }
    println(part01(input, 10))
    assert(part01(example, 40) == 2188189693529L) { "expected 2188189693529, got ${part01(example, 40)}" }
    println(part01(input, 40))
}