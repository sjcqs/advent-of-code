package puzzle12

import readInput
import split

sealed class CaveId(val id: String) {
    object Start : CaveId("start")

    class Small(id: String) : CaveId(id)

    class Big(id: String) : CaveId(id)

    object End : CaveId("end")

    override fun toString(): String = id

    override fun equals(other: Any?) = other is CaveId && other.id == id

    override fun hashCode() = id.hashCode()

    companion object {
        operator fun invoke(id: String): CaveId = when (id) {
            "start" -> Start
            "end" -> End
            else -> if (id.all { it.isUpperCase() }) Big(id) else Small(id)
        }
    }
}

fun main() {
    fun map(input: String): Map<CaveId, Set<CaveId>> {
        return input.split().map { line ->
            val (source, destination) = line.split("-")
            CaveId(source) to CaveId(destination)
        }.fold(mutableMapOf()) { map, (cave0, cave1) ->
            if (cave1 != CaveId.Start) {
                map.compute(cave0) { _, set ->
                    set?.plus(cave1) ?: setOf(cave1)
                }
            }
            if (cave0 != CaveId.Start) {
                map.compute(cave1) { _, set ->
                    set?.plus(cave0) ?: setOf(cave0)
                }
            }
            map
        }
    }

    fun visit(
        map: Map<CaveId, Set<CaveId>>,
        cave: CaveId = CaveId.Start,
        path: List<CaveId> = listOf(),
        canVisitSmallCave: (CaveId, List<CaveId>) -> Boolean
    ): Int {
        return when {
            cave == CaveId.End -> 1
            cave is CaveId.Small && !canVisitSmallCave(cave, path) -> 0
            else -> map.getValue(cave).sumOf { nextCave ->
                visit(
                    map = map,
                    cave = nextCave,
                    path = path + cave,
                    canVisitSmallCave = canVisitSmallCave
                )
            }
        }
    }

    fun part01(map: Map<CaveId, Set<CaveId>>): Int {
        return visit(map) { cave, path -> cave !in path }
    }


    fun part02(map: Map<CaveId, Set<CaveId>>): Int {
        return visit(map) { cave, path ->
            val visited = path.filterIsInstance<CaveId.Small>().groupingBy { it }.eachCount()
            visited.getOrDefault(cave, 0) < if (visited.any { it.value > 1 }) 1 else 2
        }
    }

    val example1 = readInput("example/12-1.txt").let(::map)
    val example2 = readInput("example/12-2.txt").let(::map)
    val example3 = readInput("example/12-3.txt").let(::map)
    val input = readInput("12.txt").let(::map)

    assert(part01(example1) == 10) { "got: ${part01(example1)}; expected: 10" }
    assert(part01(example2) == 19) { "got: ${part01(example2)}; expected: 19" }
    assert(part01(example3) == 226) { "got: ${part01(example3)}; expected: 226" }
    println("Part 01: ${part01(input)}")

    assert(part02(example1) == 36) { "got: ${part02(example1)}; expected: 36" }
    assert(part02(example2) == 103) { "got: ${part02(example2)}; expected: 103" }
    assert(part02(example3) == 3509) { "got: ${part02(example3)}; expected: 3509" }
    println("Part 02: ${part02(input)}")
}