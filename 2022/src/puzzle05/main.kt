package puzzle05

import readInput
import requireEquals
import split
import java.util.Deque
import java.util.LinkedList
import java.util.Stack


fun main() {
    requireEquals("CMZ", part01(map(readInput("example/05.txt"))))
    println(part01(map(readInput("05.txt"))))

    requireEquals("MCD", part02(map(readInput("example/05.txt"))))
    requireEquals("MDD", part02(map(readInput("example/05-bis.txt"))))
    println(part02(map(readInput("05.txt"))))

}

fun part02(input: Pair<Map<Int, MutableList<Char>>, List<Pair<Int, Pair<Int, Int>>>>): String {
    val (stacks, inst) = input
    inst.forEach { (count, move) ->
        val (src, dst) = move
        val srcStack = stacks.getValue(src)
        val dstStack = stacks.getValue(dst)

        val elements = srcStack.takeLast(count)
        dstStack.addAll(elements)
        repeat(count) {
            if (srcStack.isNotEmpty()) {
                srcStack.removeLast()
            }
        }

    }
    return buildString {
        stacks.values.onEach {
            if (it.isNotEmpty())
                append(it.last())
        }
    }
}

fun part01(input: Pair<Map<Int, MutableList<Char>>, List<Pair<Int, Pair<Int, Int>>>>): String {
    val (stacks, inst) = input
    inst.forEach { (count, move) ->
        val (src, dst) = move
        val srcStack = stacks.getValue(src)
        val dstStack = stacks.getValue(dst)

        repeat(count) {
            if (srcStack.isNotEmpty()) {
                val element = srcStack.removeLast()
                dstStack.add(element)
            }
        }
    }
    return buildString {
        stacks.values.onEach {
            append(it.last())
        }
    }
}

fun map(input: String): Pair<Map<Int, MutableList<Char>>, List<Pair<Int, Pair<Int, Int>>>> {
    val (stacksString, instructionsString) = input.split("\n\n")
    val stacksLines = stacksString.split()
    val ids = stacksLines.last().mapIndexedNotNull { index, c ->
        c.digitToIntOrNull()?.let { id -> id to index }
    }
    val stacks = buildMap<Int, MutableList<Char>> {
        stacksLines.dropLast(1).onEach { line ->
            ids.forEach { (id, index) ->
                compute(id) { _, value ->
                    line.getOrNull(index)
                        ?.takeIf { it.isLetter() }
                        ?.let {
                            (value ?: LinkedList()).apply {
                                add(0, it)
                            }
                        }
                }
            }
        }
    }.toSortedMap()
    val instructions = buildList {
        instructionsString
            .trim()
            .split()
            .map { instruction ->
                val (a, b, c) = "move (\\d+) from (\\d+) to (\\d+)".toRegex()
                    .matchEntire(instruction)!!.destructured
                add(Pair(a.toInt(), b.toInt() to c.toInt()))
            }
    }
    return stacks to instructions
}
