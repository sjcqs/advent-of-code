package puzzle18

import puzzle18.ShellNumber.Number
import puzzle18.ShellNumber.Regular
import readInput
import split
import kotlin.math.ceil
import kotlin.math.floor

sealed class ShellNumber(var parent: Number? = null) {

    operator fun plus(other: ShellNumber): ShellNumber {
        return Number(this, other)
    }

    data class Number(var left: ShellNumber, var right: ShellNumber) : ShellNumber() {
        init {
            left.parent = this
            right.parent = this
        }

        fun replace(old: ShellNumber, new: ShellNumber) {
            if (left === old) {
                left = new
            } else {
                right = new
            }
            new.parent = this
        }

        override fun toString() = "[$left,$right]"
    }

    data class Regular(var value: Int) : ShellNumber() {
        override fun toString() = "$value"
    }
}

private fun String.toShellNumber(): ShellNumber {
    var cursor = 1
    val leftElement = with(drop(cursor)) {
        val numberOrNull = takeWhile { it.isDigit() }.toIntOrNull()
        when {
            numberOrNull != null -> Regular(numberOrNull)
            else -> toShellNumber()
        }
    }
    cursor += leftElement.stringLength + 1
    val rightElement = with(drop(cursor)) {
        val numberOrNull = takeWhile { it.isDigit() }.toIntOrNull()
        when {
            numberOrNull != null -> Regular(numberOrNull)
            else -> toShellNumber()
        }
    }
    return Number(leftElement, rightElement)
}

private fun Regular.split() {
    val left = floor(value / 2f).toInt()
    val right = ceil(value / 2f).toInt()
    val newNum = Number(Regular(left), Regular(right))

    parent?.replace(this, newNum)
}

private fun Number.explode() {
    firstNonSideParent(Number::left)?.left?.rightMost()
        ?.apply { value += (left as Regular).value }
    firstNonSideParent(Number::right)?.right?.leftMost()
        ?.apply { value += (right as Regular).value }

    parent?.replace(this, Regular(0))
}

val ShellNumber.magnitude: Long
    get() {
        return when (this) {
            is Regular -> value.toLong()
            is Number -> left.magnitude * 3 + right.magnitude * 2
        }
    }

fun ShellNumber.reduce(): ShellNumber {
    val canExplode = this.findExploding()
    if (canExplode != null) {
        canExplode.explode()
        return reduce()
    }
    val canSplit = this.findSplitting()
    if (canSplit != null) {
        canSplit.split()
        return reduce()
    }
    return this
}

private fun ShellNumber.findExploding(depth: Int = 1): Number? {
    return when (this) {
        is Regular -> null
        is Number -> {
            if (depth > 4) {
                this
            } else {
                left.findExploding(depth + 1) ?: right.findExploding(depth + 1)
            }
        }
    }
}

private fun ShellNumber.findSplitting(): Regular? {
    return when (this) {
        is Regular -> if (value >= 10) this else null
        is Number -> left.findSplitting() ?: right.findSplitting()
    }
}

private val ShellNumber.stringLength: Int
    get() = when (this) {
        is Number -> 3 + (left.stringLength + right.stringLength)
        is Regular -> value.toString().length
    }

private fun ShellNumber.firstNonSideParent(side: Number.() -> ShellNumber): Number? {
    var current = this

    while (current.parent != null) {
        if (current.parent!!.side() !== current) {
            return current.parent
        } else {
            current = current.parent!!
        }
    }

    return null
}

private fun ShellNumber.rightMost(): Regular {
    return when (this) {
        is Regular -> this
        is Number -> right.rightMost()
    }
}

private fun ShellNumber.leftMost(): Regular {
    return when (this) {
        is Regular -> this
        is Number -> left.leftMost()
    }
}

fun <E> permutations(list: List<E>, length: Int? = null): Sequence<List<E>> = sequence {
    val size = list.size
    val r = length ?: list.size

    val indices = list.indices.toMutableList()
    val cycles = (size downTo (size - r)).toMutableList()
    yield(indices.take(r).map { list[it] })

    while (true) {
        var broke = false
        for (i in (r - 1) downTo 0) {
            cycles[i]--
            if (cycles[i] == 0) {
                val end = indices[i]
                for (j in i until indices.size - 1) {
                    indices[j] = indices[j + 1]
                }
                indices[indices.size - 1] = end
                cycles[i] = size - i
            } else {
                val j = cycles[i]
                val tmp = indices[i]
                indices[i] = indices[-j + indices.size]
                indices[-j + indices.size] = tmp
                yield(indices.take(r).map { list[it] })
                broke = true
                break
            }
        }
        if (!broke) {
            break
        }
    }
}

fun main() {
    fun map(input: String) = input.split().map(String::toShellNumber)

    fun part01(input: String): Long {
        return map(input)
        .reduce { acc, number -> (acc + number).reduce() }
            .magnitude
    }

    fun part02(input: String): Long {
        return permutations(input.split(), length = 2)
            .maxOf { pair ->
                val (first, second) = pair.map { it.toShellNumber() }
                (first + second).reduce().magnitude
            }
    }

    val input = readInput("18.txt")
    println(part01(input))
    println(part02(input))
}