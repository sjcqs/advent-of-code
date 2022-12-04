package puzzle04

import readInput
import requireEquals


fun main() {
    val example = map(readInput("example/04.txt"))
    val input = map(readInput("04.txt"))

    requireEquals(2, part01(example))
    requireEquals(4, part02(example))

    println(part01(input))
    println(part02(input))

}

private fun map(input: String) = input
    .trim()
    .split("\n")
    .map { pair ->
        val (a, b) = pair.split(",")
        Pair(a.toRange(), b.toRange())
    }

private fun String.toRange(): IntRange {
    val (left, right) = split("-")
    return left.toInt()..right.toInt()
}


private fun part01(input: List<Pair<IntRange, IntRange>>): Int {
    return input
        .count { (a, b) ->
            val intersection = a.intersectRange(b)
            println(intersection)
            intersection == a || intersection == b
        }
}

fun IntRange.intersectRange(other: IntRange): IntRange? {
    val set = intersect(other).sorted()
    if (set.isEmpty()) {
        return null
    }
    return set.first()..set.last()
}

fun part02(input: List<Pair<IntRange, IntRange>>): Int {
    return input.count { (a, b) ->
        val intersection = a.intersectRange(b)
        println(intersection)
        intersection != null
    }
}