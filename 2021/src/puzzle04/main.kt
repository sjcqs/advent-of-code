package puzzle04

import readInput
import split

fun main() {
    val example = readInput("example/04.txt").let(::map)
    val input = readInput("04.txt").let(::map)

    assert(part01(example) == 4512)
    assert(part02(example) == 1924)

    println(part01(input))
    println(part02(input))

}

private fun part01(input: Input): Int {
    val gridsScores = input.grids
        .mapNotNull { it.isBingo(input.numbers) }
        .sortedBy(Bingo::turn)
    return gridsScores.first().score
}

private fun part02(input: Input): Int {
    val gridsScores = input.grids
        .mapNotNull { it.isBingo(input.numbers) }
        .sortedBy(Bingo::turn)
    return gridsScores.last().score
}


private data class Input(
    val numbers: Set<Int>,
    val grids: List<List<List<Int>>>
)

private data class Bingo(
    val turn: Int,
    val score: Int
)

fun List<List<Int>>.toBingoGrid(): List<MutableMap<Int, Boolean>> = map { it.associateWith { false }.toMutableMap() }

private fun List<List<Int>>.isBingo(numbers: Set<Int>): Bingo? {
    val bingoGrid = toBingoGrid()
    numbers.forEachIndexed { index, number ->
        bingoGrid.forEach { rows -> rows.replace(number, true) }

        val isAnyRowBingo = bingoGrid.any { rows -> rows.values.all { it } }
        val isAnyColumnBingo = indices.any { column -> bingoGrid.all { rows -> rows.values.toList()[column] } }

        if (isAnyRowBingo || isAnyColumnBingo) {
            val unmarkedSum = bingoGrid.sumOf {
                it.mapNotNull { (number, isMarked) -> number.takeIf { !isMarked } }.sum()
            }
            return Bingo(index, (unmarkedSum * number))
        }
    }

    return null
}

private fun map(input: String): Input {
    val lines = input.split()

    val numbers = lines.first().split(",").map(String::toInt).toSet()

    val grids = lines.drop(1).chunked(6).map { gridLines ->
        gridLines.drop(1).map {
            it.trim().split("\\s+".toRegex()).map(String::toInt)
        }
    }

    return Input(numbers, grids)
}