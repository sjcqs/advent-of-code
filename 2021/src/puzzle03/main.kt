package puzzle03

import readInput
import split
import java.util.*

fun main() {
    val example = readInput("example/03.txt").let(::map)
    val input = readInput("03.txt").let(::map)

    assert(part01(example) == 198)
    assert(part02(example) == 230)

    println(part01(input))
    println(part02(input))

}

fun map(input: String): List<Int> {
    return input.split().map { it.toInt(2) }
}

fun part01(input: List<Int>): Int {
    val frequencies = getBitFrequencies(input)
    val (gamma, epsilon) = (frequencies.lastKey() downTo 0).fold(0 to 0) { (gamma, epsilon), position ->
        val frequency = frequencies.getOrDefault(position, 0)
        frequency?.let {
            val gammaBit = if (frequency >= input.size / 2f) 1 else 0
            val epsilonBit = if (frequency >= input.size / 2f) 0 else 1

            ((gamma shl 1) or gammaBit) to ((epsilon shl 1) or epsilonBit)
        } ?: (gamma to epsilon)
    }
    return gamma * epsilon
}

fun part02(input: List<Int>): Int {
    val frequencies = getBitFrequencies(input)
    val oxygenRating = findOxygenRating(input, frequencies)
    val co2Rating = findCO2Rating(input, frequencies)
    return oxygenRating * co2Rating
}

private fun getBitFrequencies(input: List<Int>): SortedMap<Int, Int> {
    val frequencies = input.fold(mutableMapOf<Int, Int>()) { frequencies, number ->
        (0 until Int.SIZE_BITS).forEach { position ->
            val bit = number.shr(position) and 0x1
            if (bit == 1) {
                frequencies.compute(position) { _, count -> count?.inc() ?: 1 }
            }
        }
        frequencies
    }.toSortedMap()
    return frequencies
}


private fun findCO2Rating(input: List<Int>, frequencies: SortedMap<Int, Int>): Int {
    val filteredInput = input.toMutableList()
    (frequencies.lastKey() downTo 0).forEach { position ->
        val newFrequencies = getBitFrequencies(filteredInput)
        val frequency = newFrequencies.getOrDefault(position, 0)
        if (frequency >= filteredInput.size / 2f) {
            filteredInput.removeIf { number ->
                (number.shr(position) and 0x1 != 0)
            }
        } else {
            filteredInput.removeIf { number ->
                (number.shr(position) and 0x1 != 1)
            }
        }
        if (filteredInput.size == 1) {
            return filteredInput.first()
        }
    }
    return filteredInput.first()
}

private fun findOxygenRating(input: List<Int>, frequencies: SortedMap<Int, Int>): Int {
    val filteredInput = input.toMutableList()
    (frequencies.lastKey() downTo 0).forEach { position ->
        val newFrequencies = getBitFrequencies(filteredInput)
        val frequency = newFrequencies.getOrDefault(position, 0)
        if (frequency >= filteredInput.size / 2f) {
            filteredInput.removeIf { number ->
                (number.shr(position) and 0x1 != 1)
            }
        } else {
            filteredInput.removeIf { number ->
                (number.shr(position) and 0x1 != 0)
            }
        }
        if (filteredInput.size == 1) {
            return filteredInput.first()
        }
    }
    return filteredInput.first()
}