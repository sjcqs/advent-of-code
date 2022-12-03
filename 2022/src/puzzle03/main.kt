package puzzle03

import readInput

val priorities = buildMap {
    putAll(('a'..'z').mapIndexed { index, c -> c to (index + 1) })
    putAll(('A'..'Z').mapIndexed { index, c -> c to (index + 27) })
}

fun main() {
    val example1 = map01(readInput("example/03.txt"))
    val input1 = map01(readInput("03.txt"))
    val example2 = map02(readInput("example/03.txt"))
    val input2 = map02(readInput("03.txt"))

    require(part01(example1) == 157)
    require(part02(example2).also { println(it) } == 70)

    println(part01(input1))
    println(part02(input2))

}

private fun map01(input: String) = input
    .trim()
    .split("\n")
    .map { backpack ->
        val items = backpack.toList()
        val a = items.take(items.size / 2)
        val b = items.takeLast(items.size / 2)
        a.toSet() to b.toSet()
    }

private fun map02(input: String): List<Triple<Set<Char>, Set<Char>, Set<Char>>> = input
    .trim()
    .split("\n")
    .map { backpack -> backpack.toList() }
    .chunked(3)
    .map { group ->
        val (a, b, c) = group
        Triple(a.toSet(), b.toSet(), c.toSet())
    }


fun part01(input: List<Pair<Set<Char>, Set<Char>>>): Int {
    return input.sumOf { (a, b) ->
        a.intersect(b)
            .sumOf { priorities.getValue(it) }
    }
}

fun part02(input: List<Triple<Set<Char>, Set<Char>, Set<Char>>>): Int {
    return input.sumOf { (a, b, c) ->
        a.intersect(b)
            .intersect(c)
            .sumOf { priorities.getValue(it) }
    }
}