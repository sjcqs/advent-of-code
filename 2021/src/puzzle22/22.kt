package puzzle22

import readInput
import split

data class SingleCube(val x: Int, val y: Int, val z: Int)
data class Cube(
    val x1: Int, val x2: Int, val y1: Int, val y2: Int, val z1: Int, val z2: Int
) {

    fun isValid(): Boolean {
        return x1 <= x2 && y1 <= y2 && z1 <= z2
    }

    fun contains(other: Cube): Boolean {
        return x1 <= other.x1 && x2 >= other.x2 && y1 <= other.y1 && y2 >= other.y2 && z1 <= other.z1 && z2 >= other.z2
    }

    infix fun intersect(other: Cube): Cube? {
        return Cube(
            maxOf(x1, other.x1),
            minOf(x2, other.x2),
            maxOf(y1, other.y1),
            minOf(y2, other.y2),
            maxOf(z1, other.z1),
            minOf(z2, other.z2)
        ).takeIf { it.isValid() }
    }

    fun volume(): Long {
        return (x2 - x1 + 1).toLong() * (y2 - y1 + 1).toLong() * (z2 - z1 + 1).toLong()
    }

    operator fun minus(other: Cube): List<Cube> {
        val intersection = this intersect other
        if (intersection == null) return listOf(this)
        else if (intersection == this) return emptyList()
        return buildList {
            if (x1 < intersection.x1) {
                add(copy(x2 = intersection.x1 - 1))
            }
            if (x2 > intersection.x2) {
                add(copy(x1 = intersection.x2 + 1))
            }
            if (y1 < intersection.y1) {
                add(copy(x1 = intersection.x1, x2 = intersection.x2, y2 = intersection.y1 - 1))
            }
            if (y2 > intersection.y2) {
                add(copy(x1 = intersection.x1, x2 = intersection.x2, y1 = intersection.y2 + 1))
            }
            if (z1 < intersection.z1) {
                add(intersection.copy(z1 = z1, z2 = intersection.z1 - 1))
            }
            if (z2 > intersection.z1) {
                add(intersection.copy(z1 = intersection.z2 + 1, z2 = z2))
            }
        }
    }
}

val List<Cube>.volume get() = sumOf { it.volume() }
operator fun List<Cube>.minus(cube: Cube) = flatMap { it - cube }

data class Instruction(
    val turnOn: Boolean, val x1: Int, val x2: Int, val y1: Int, val y2: Int, val z1: Int, val z2: Int
) {
    override fun toString(): String {
        return buildString {
            if (turnOn) {
                append("on ")
            } else {
                append("off ")
            }
            append("x=$x1..$x2,y=$y1..$y2,z=$z1..$z2")
        }
    }
}

fun Regex.requireMatchEntire(input: CharSequence) = matchEntire(input) ?: error("$input did not match $this")

fun map(input: String): List<Instruction> {
    val onRegex = "on x=(-?\\d+)..(-?\\d+),y=(-?\\d+)..(-?\\d+),z=(-?\\d+)..(-?\\d+)".toRegex()
    val offRegex = "off x=(-?\\d+)..(-?\\d+),y=(-?\\d+)..(-?\\d+),z=(-?\\d+)..(-?\\d+)".toRegex()
    return input.split().map { line ->
        when {
            line.startsWith("on") -> {
                val (xFirst, xLast, yFirst, yLast, zFirst, zLast) = onRegex.requireMatchEntire(line).destructured
                Instruction(
                    turnOn = true,
                    x1 = xFirst.toInt(), x2 = xLast.toInt(),
                    y1 = yFirst.toInt(), y2 = yLast.toInt(),
                    z1 = zFirst.toInt(), z2 = zLast.toInt(),
                )
            }
            else -> {
                val (xFirst, xLast, yFirst, yLast, zFirst, zLast) = offRegex.requireMatchEntire(line).destructured
                Instruction(
                    turnOn = false,
                    x1 = xFirst.toInt(), x2 = xLast.toInt(),
                    y1 = yFirst.toInt(), y2 = yLast.toInt(),
                    z1 = zFirst.toInt(), z2 = zLast.toInt(),
                )
            }
        }
    }
}

fun main() {
    fun part01(input: List<Instruction>): Int {
        val instructions = input.filter {
            it.x1 in -50..50 && it.y1 in -50..50 && it.z1 in -50..50 && it.x2 in -50..50 && it.y2 in -50..50 && it.z2 in -50..50
        }
        val turnedOnSingleCubes = mutableSetOf<SingleCube>()

        instructions.forEach { instruction ->
            if (instruction.turnOn) {
                turnedOnSingleCubes.addAll((instruction.x1..instruction.x2).flatMap { x ->
                    (instruction.y1..instruction.y2).flatMap { y ->
                        (instruction.z1..instruction.z2).map { z -> SingleCube(x, y, z) }
                    }
                })
            } else {
                turnedOnSingleCubes.removeIf {
                    it.x in instruction.x1..instruction.x2 && it.y in instruction.y1..instruction.y2 && it.z in instruction.z1..instruction.z2
                }
            }
        }
        return turnedOnSingleCubes.size
    }

    fun part02(input: List<Instruction>): Long {
        var cubes = listOf<Cube>()

        input.forEach { instruction ->
            val instructionCube = Cube(
                x1 = instruction.x1,
                x2 = instruction.x2,
                y1 = instruction.y1,
                y2 = instruction.y2,
                z1 = instruction.z1,
                z2 = instruction.z2
            )
            cubes = cubes - instructionCube
            if (instruction.turnOn) {
                cubes = cubes + instructionCube
            }
        }
        return cubes.volume
    }

    val examples = listOf(
        "example/22-1.txt",
        "example/22-2.txt",
    ).map { map(readInput(it)) }
    val input = map(readInput("22.txt"))

    println(part01(input))

    val example = map(readInput("example/22-3.txt"))
    println(part02(example))
    println(part02(input))
}

