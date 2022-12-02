package puzzle02

import readInput

enum class Symbol(val score: Int) {
    Rock(1), Paper(2), Scissors(3);

    fun result(other: Symbol) = when (this) {
        Rock -> when (other) {
            Rock -> Result.Draw
            Paper -> Result.Lose
            Scissors -> Result.Win
        }
        Paper -> when (other) {
            Rock -> Result.Win
            Paper -> Result.Draw
            Scissors -> Result.Lose
        }
        Scissors -> when (other) {
            Rock -> Result.Lose
            Paper -> Result.Win
            Scissors -> Result.Draw
        }
    }
}

enum class Result(val score: Int) {
    Win(6), Draw(3), Lose(0);

    fun symbol(opponent: Symbol) = when(this) {
        Win -> when(opponent){
            Symbol.Rock -> Symbol.Paper
            Symbol.Paper -> Symbol.Scissors
            Symbol.Scissors -> Symbol.Rock
        }
        Draw -> opponent
        Lose -> when(opponent){
            Symbol.Rock -> Symbol.Scissors
            Symbol.Paper -> Symbol.Rock
            Symbol.Scissors -> Symbol.Paper
        }
    }
}

val playerMap = mapOf(
    'X' to Symbol.Rock,
    'Y' to Symbol.Paper,
    'Z' to Symbol.Scissors
)
val resultMap = mapOf(
    'X' to Result.Lose,
    'Y' to Result.Draw,
    'Z' to Result.Win
)
val opponentMap = mapOf(
    'A' to Symbol.Rock,
    'B' to Symbol.Paper,
    'C' to Symbol.Scissors
)

fun score(opponent: Symbol, player: Symbol): Int {
    return player.score + player.result(opponent).score
}

fun score(opponent: Symbol, player: Result): Int {
    return player.score + player.symbol(opponent).score
}

fun main() {
    val example = readInput("example/02.txt").let(::map)
    val input = readInput("02.txt").let(::map)

    assert(part01(example) == 15)
    assert(part02(example) == 12)

    println(part01(input))
    println(part02(input))

}

fun map(input: String): List<Pair<Char, Char>> {
    return input.split("\n").map { round ->
        val opponent = round[0]
        val player = round[2]

        opponent to player
    }
}

fun part01(input: List<Pair<Char, Char>>): Int {
    return input.sumOf {
        score(opponent = opponentMap.getValue(it.first), player = playerMap.getValue(it.second))
    }

}

fun part02(input: List<Pair<Char, Char>>): Int {
    return input.sumOf {
        score(opponent = opponentMap.getValue(it.first), player = resultMap.getValue(it.second))
    }
}