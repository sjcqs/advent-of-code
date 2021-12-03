package puzzle01

import readInput
import split

fun main() {
    val example = readInput("example/01.txt").let(::map)
    val input = readInput("01.txt").let(::map)

    assert(part01(example) == 7)
    assert(part02(example) == 5)

    println(part01(input))
    println(part02(input))

}

fun map(input: String): List<Long> {
    return input.split().map(String::toLong)
}

fun part01(input: List<Long>): Int {
    return input.windowed(2)
        .count { (first, second) -> second > first }
}

fun part02(input: List<Long>): Int {
    return input.asSequence()
        .windowed(3)
        .filter { window -> window.size == 3 }
        .map { window -> window.sum() }
        .windowed(2)
        .count { (first, second) -> second > first }
}