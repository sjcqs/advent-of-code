package puzzle17

import InputMapper
import Puzzle

object Puzzle17 : Puzzle<Puzzle17.Input, Puzzle17.Output>(
    InputMapper { input ->
        val cubes = mutableSetOf<Point>()
        input.split("\n").forEachIndexed { y, line ->
            line.forEachIndexed { x, cube ->
                if (cube == '#') {
                    cubes.add(Point(x, y, 0, 0))
                }
            }
        }
        Input(cubes)
    }
) {

    override val inputFileName = "17.txt"

    override suspend fun doJob(input: Input): Output {
        return Output(
            Dimension(input.initialSlice).simulate(6, 3),
            Dimension(input.initialSlice).simulate(6, 4)
        )
    }


    data class Input(val initialSlice: Set<Point>)

    data class Output(
        val litCubesAfterIterations: Int,
        val litCubesAfterIterationsFourthDimension: Int
    )

    class Dimension(initialSlice: Set<Point>) {
        private val cubes: MutableMap<Point, Boolean> = initialSlice.associateWith { true }.toMutableMap()

        fun simulate(cycles: Int, dimension: Int): Int {
            repeat(cycles) {
                display(dimension)
                simulate(dimension)
            }
            return cubes.filterValues { it }.size
        }

        private fun display(dimension: Int) {
            val min = cubes.keys.filter { it.isLit }.let { cubes ->
                Point(
                    cubes.minOf { it.x },
                    if (dimension > 1) cubes.minOf { it.y } else 0,
                    if (dimension > 2) cubes.minOf { it.z } else 0,
                    if (dimension > 3) cubes.minOf { it.w } else 0,
                )

            }
            val max = cubes.keys.filter { it.isLit }.let { cubes ->
                Point(
                    cubes.maxOf { it.x },
                    if (dimension > 1) cubes.maxOf { it.y } else 0,
                    if (dimension > 2) cubes.maxOf { it.z } else 0,
                    if (dimension > 3) cubes.maxOf { it.w } else 0,
                )
            }
            val state = buildString {
                (min.z..max.z).forEach { z ->
                    appendLine("($min -> $max)")
                    (min.w..max.w).forEach { w ->
                        appendLine("z=$z w=$w ")
                        (min.y..max.y).forEach { y ->
                            (min.x..max.x).forEach { x ->
                                val char = if (this@Dimension.cubes[Point(x, y, z, w)] == true) {
                                    '#'
                                } else {
                                    '.'
                                }
                                append(char)
                            }
                            appendLine()
                        }
                    }
                }
            }
            log(state)
        }

        private fun simulatedCubes(dimension: Int): List<Point> {
            val min = cubes.keys.let { cubes ->
                Point(
                    cubes.minOf { it.x } - 1,
                    if (dimension > 1) cubes.minOf { it.y } - 1 else 0,
                    if (dimension > 2) cubes.minOf { it.z } - 1 else 0,
                    if (dimension > 3) cubes.minOf { it.w } - 1 else 0,
                )

            }
            val max = cubes.keys.let { cubes ->
                Point(
                    cubes.maxOf { it.x } + 1,
                    if (dimension > 1) cubes.maxOf { it.y } + 1 else 0,
                    if (dimension > 2) cubes.maxOf { it.z } + 1 else 0,
                    if (dimension > 3) cubes.maxOf { it.w } + 1 else 0,
                )
            }
            return (min.w..max.w).flatMap { w ->
                (min.z..max.z).flatMap { z ->
                    (min.y..max.y).flatMap { y ->
                        (min.x..max.x).map { x ->
                            Point(x, y, z, w)
                        }
                    }
                }
            }
        }

        private fun simulate(dimension: Int) {
            val newCubes = cubes.toMutableMap()
            val simulatedCubes = simulatedCubes(dimension)
            simulatedCubes.forEach { cube ->
                val litNeighbours = cube.getNeighbours(dimension)

                if (cube.isLit) {
                    newCubes[cube] = litNeighbours.size in 2..3
                } else if (!cube.isLit) {
                    newCubes[cube] = litNeighbours.size == 3
                }
            }
            cubes.clear()
            cubes.putAll(newCubes)
        }

        private fun Point.getNeighbours(dimension: Int): List<Point> {
            return (x - 1..x + 1).flatMap { currentX ->
                if (dimension == 1) {
                    listOf(Point(currentX, 0, 0, 0))
                } else {
                    (y - 1..y + 1).flatMap { currentY ->
                        if (dimension == 2) {
                            listOf(Point(currentX, currentY, 0, 0))
                        } else {
                            (z - 1..z + 1).flatMap { currentZ ->
                                if (dimension == 3) {
                                    listOf(Point(currentX, currentY, currentZ, 0))
                                } else {
                                    (w - 1..w + 1).map { currentW ->
                                        Point(currentX, currentY, currentZ, currentW)
                                    }
                                }
                            }
                        }
                    }
                }
            }.filterNot { it == this }
                .filter { it.isLit }
        }

        private val Point.isLit: Boolean
            get() = cubes.getOrDefault(this, false)
    }

    data class Point(val x: Int, val y: Int, val z: Int, val w: Int)
}