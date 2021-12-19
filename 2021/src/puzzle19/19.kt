package puzzle19

import readInput
import split
import kotlin.math.abs

data class Point(
    val x: Int,
    val y: Int,
    val z: Int
) {
    val permutations: Sequence<Point> = sequence {
        yield(Point(+x, +y, +z))
        yield(Point(+y, +z, +x))
        yield(Point(+z, +x, +y))
        yield(Point(+z, +y, -x))
        yield(Point(+y, +x, -z))
        yield(Point(+x, +z, -y))

        yield(Point(+x, -y, -z))
        yield(Point(+y, -z, -x))
        yield(Point(+z, -x, -y))
        yield(Point(+z, -y, +x))
        yield(Point(+y, -x, +z))
        yield(Point(+x, -z, +y))

        yield(Point(-x, +y, -z))
        yield(Point(-y, +z, -x))
        yield(Point(-z, +x, -y))
        yield(Point(-z, +y, +x))
        yield(Point(-y, +x, +z))
        yield(Point(-x, +z, +y))

        yield(Point(-x, -y, +z))
        yield(Point(-y, -z, +x))
        yield(Point(-z, -x, +y))
        yield(Point(-z, -y, -x))
        yield(Point(-y, -x, -z))
        yield(Point(-x, -z, -y))
    }
}

fun Point.diff(other: Point) = Point(
    x - other.x,
    y - other.y,
    z - other.z
)

operator fun Point.plus(other: Point) = Point(
    x + other.x,
    y + other.y,
    z + other.z
)

fun Point.manhattanDistance(other: Point) = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)

fun common(points: List<Point>, otherPoints: List<Point>, diff: Point): Int {
    val intersect = points.intersect(otherPoints.map { it + diff }.toSet())
    return intersect.size
}

fun match(beaconsA: List<Point>, beaconsB: List<Point>): Pair<List<Point>, Point>? {
    beaconsB.fold(mutableListOf<List<Point>>()) { acc, point ->
        point.permutations.forEachIndexed { index, permutedPoint ->
            val currentList = acc.getOrNull(index)
            if (currentList != null) {
                acc[index] = currentList.plus(permutedPoint)
            } else {
                acc.add(index, listOf(permutedPoint))
            }
        }
        acc
    }.forEach { permutedBeaconsB ->
        val maybeDiff = findDiff(beaconsA, permutedBeaconsB)
        if (maybeDiff != null) {
            return permutedBeaconsB.map { it + maybeDiff } to maybeDiff
        }
    }
    return null
}

fun findDiff(beaconsA: List<Point>, beaconsB: List<Point>): Point? {
    beaconsA.forEachIndexed { index, beaconA ->
        (0 until index).map(beaconsB::get).forEach { beaconB ->
            val diff = beaconA.diff(beaconB)
            if (common(beaconsA, beaconsB, diff) >= 12) {
                return diff
            }
        }
    }
    return null
}

fun findBeacons(scanners: List<List<Point>>): Pair<Collection<Point>, List<Point>> {
    val scannersLeft = scanners.toMutableList()
    val detectedBeacons = mutableListOf(scannersLeft.removeFirst())
    val diffs = mutableListOf<Point>()

    while (scannersLeft.isNotEmpty()) {
        val toRemove = mutableListOf<List<Point>>()
        scannersLeft.forEachIndexed { _, points ->
            detectedBeacons.forEach { detectedBeacon ->
                val matched = match(detectedBeacon, points)
                if (matched != null) {
                    val (beacons, diff) = matched
                    toRemove.add(points)
                    detectedBeacons.add(beacons)
                    diffs.add(diff)
                    return@forEachIndexed
                }
            }
        }
        toRemove.forEach { scannersLeft.remove(it) }
    }

    return detectedBeacons.flatten().toSet() to diffs
}

fun map(input: String): List<List<Point>> {
    return input.split("\n\n").map { scannerLines ->
        scannerLines.split().drop(1).map {
            val (x, y, z) = it.split(",")
            Point(x.toInt(), y.toInt(), z.toInt())
        }
    }
}

fun main() {
    fun part01(input: Collection<Point>): Int {
        return input.size
    }

    fun part02(diffs: List<Point>): Int {
        return diffs.flatMap { a -> diffs.map { b -> a to b } }.maxOf { (a, b) -> a.manhattanDistance(b) }
    }

    val example = map(readInput("example/19.txt"))
    val (exampleBeacons, exampleDiffs) = findBeacons(example)
    assert(part01(exampleBeacons) == 79) { "expected 79, got ${part01(exampleBeacons)}" }
    assert(part02(exampleDiffs) == 3621) { "expected 3621, got ${part02(exampleDiffs)}" }

    val input = map(readInput("19.txt"))
    val (beacons, diffs) = findBeacons(input)
    println("Part 01: ${part01(beacons)}")
    println("Part 02: ${part02(diffs)}")
}
