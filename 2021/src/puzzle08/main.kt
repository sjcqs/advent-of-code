package puzzle08

import readInput
import split
import kotlin.math.sign

fun main() {
    val uniqueSegmentsCount = mapOf(
        2 to 0x1,
        4 to 0x4,
        3 to 0x7,
        7 to 0x8,
    )
    val segmentsCount = mapOf(
        2 to setOf(0x1),
        4 to setOf(0x4),
        3 to setOf(0x7),
        7 to setOf(0x8),
        5 to setOf(0x2, 0x3),
        6 to setOf(0x0, 0x6, 0x9)
    )

    val normalSegments = mapOf(
        0 to setOf('a', 'b', 'c', 'e', 'f', 'g'),
        1 to setOf('c', 'f'),
        2 to setOf('a', 'c', 'd', 'e', 'g'),
        3 to setOf('a', 'c', 'd', 'f', 'g'),
        4 to setOf('b', 'c', 'd', 'f'),
        5 to setOf('a', 'b', 'd', 'f', 'g'),
        6 to setOf('a', 'b', 'd', 'e', 'f', 'g'),
        7 to setOf('a', 'c', 'f'),
        8 to setOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
        9 to setOf('a', 'b', 'c', 'd', 'f', 'g'),
    )

    data class Entry(
        val signals: List<List<Char>>,
        val outputs: List<List<Char>>
    )

    fun List<List<Char>>.ofSize(size: Int) = filter { it.size == size }

    fun Entry.decode(): Int {
        val wiring = mutableMapOf<Int, List<Char>>()
        sequenceOf(1, 4, 7, 8, 2, 3, 5, 6, 9, 0).forEach { digit ->
            when (digit) {
                0 -> wiring[0] = signals.ofSize(6).single { it !in listOf(wiring.getValue(6), wiring.getValue(9)) }
                1 -> wiring[1] = signals.ofSize(2).single()
                2 -> wiring[2] = signals.ofSize(5).single { it.filter { wire -> wire in wiring.getValue(4) }.size == 2 }
                3 -> wiring[3] = signals.ofSize(5).single { it.containsAll(wiring.getValue(1)) }
                4 -> wiring[4] = signals.ofSize(4).single()
                5 -> wiring[5] = signals.ofSize(5).single { it !in listOf(wiring.getValue(2), wiring.getValue(3)) }
                6 -> wiring[6] = signals.ofSize(6).single { !it.containsAll(wiring.getValue(1)) }
                7 -> wiring[7] = signals.ofSize(3).single()
                8 -> wiring[8] = signals.ofSize(7).single()
                9 -> wiring[9] = signals.ofSize(6).single { it.containsAll(wiring.getValue(4)) }
                else -> Unit
            }
        }
        return wiring.entries.associate { (digit, chars) -> chars.sorted() to digit }
            .let { digitsMapping ->
                outputs.map { digitsMapping.getValue(it.sorted()) }.joinToString("").toInt()
            }
    }

    fun map(input: String): List<Entry> {
        return input.split().map { line ->
            val (patterns, output) = line.split(" | ").map { it -> it.split(" ").map { it.toList() } }
            Entry(patterns, output)
        }
    }

    fun part01(input: List<Entry>): Int {
        return input
            .flatMap { it.outputs }
            .mapNotNull { output -> uniqueSegmentsCount[output.size] }
            .size
    }

    fun part02(input: List<Entry>): Int {
        return input.sumOf { it.decode().also(::println) }
    }


    val example1 = readInput("example/08-1.txt").let(::map)
    val example2 = readInput("example/08-2.txt").let(::map)
    val input = readInput("08.txt").let(::map)

    assert(part01(example1) == 0) { "part01(example1) should be 0, got ${part01(example1)}" }
    assert(part01(example2) == 26) { "part01(example2) should be 26, got ${part01(example2)}" }
    println(part01(input))

    assert(part02(example1) == 5353) { "part02(example1) should be 5353, got ${part02(example1)}" }
    assert(part02(example2) == 61229) { "part02(example2) should be 61229, got ${part02(example2)}" }
    println(part02(input))
}