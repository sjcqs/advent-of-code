package puzzle07

import readInput
import kotlin.math.abs

fun main() {
    val example = readInput("example/07.txt").let(::map)
    val input = readInput("07.txt").let(::map)

    assert(part01(example) == 37)
    println("Part 1: ${part01(input)}")

    assert(part02(example) == 168)
    println("Part 2: ${part02(input)}")
}

fun map(input: String): List<Int> = input.split(",").map { it.toInt() }

fun part01(input: List<Int>): Int {
    return totalFuelCost(input) { source, destination -> abs(source - destination) }
}

fun part02(input: List<Int>): Int {
    val fuelCost = generateSequence(1) { 1 + it }
    val cache = mutableMapOf<Int, Int>()
    return totalFuelCost(input) { source, destination ->
        val distance = abs(source - destination)
        cache.getOrPut(distance) { fuelCost.take(distance).sum() }
    }
}

fun totalFuelCost(input: List<Int>, getFuelCost: (source: Int, destination: Int) -> Int): Int {
    val (min, max) = input.minOrNull() to input.maxOrNull()
    if (min == null || max == null) {
        return -1
    }

    return (min..max).minOf { destination ->
        input.sumOf { source -> getFuelCost(source, destination) }
    }
}