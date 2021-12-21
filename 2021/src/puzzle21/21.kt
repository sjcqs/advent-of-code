package puzzle21

import readInput
import split

fun map(input: String): Pair<Int, Int> {
    val (first, second) = input.split().map {
        val (_, position) = it.split(": ")
        position.toInt()
    }

    return first to second
}

fun main() {
    fun part01(input: Pair<Int, Int>): Long {
        val die = generateSequence(1) { if (it + 1 > 100) 1 else it + 1 }.iterator()
        var (player1, player2) = input
        var (player1Score, player2Score) = 0 to 0
        var rolls = 0
        fun rollDie(): Int {
            rolls += 3
            val first = die.next()
            val second = die.next()
            val third = die.next()

            return (first + second + third)
        }
        while (player1Score < 1000 || player2Score < 1000) {
            val player1Roll = rollDie()
            repeat(player1Roll) {
                player1++
                if (player1 > 10) player1 = 1
            }
            player1Score += player1
            if (player1Score >= 1000) break
            val player2Roll = rollDie()
            repeat(player2Roll) {
                player2++
                if (player2 > 10) player2 = 1
            }
            player2Score += player2
            if (player2Score >= 1000) break
        }
        return minOf(player1Score, player2Score) * rolls.toLong()
    }

    fun part02(input: Pair<Int, Int>): Long {
        data class Universe(
            val position1: Int,
            val position2: Int,
            val score1: Int,
            val score2: Int,
            val turn: Int
        )

        fun roll(universe: Universe, nextRoll: Int): Universe {
            var position1: Int = universe.position1
            var position2: Int = universe.position2
            var score1: Int = universe.score1
            var score2: Int = universe.score2
            var turn: Int = universe.turn
            if (universe.turn < 3) {
                repeat(nextRoll) {
                    position1++
                    if (position1 > 10) position1 = 1
                }
                if (turn == 2) { // last player 1 turn
                    score1 += position1
                }
            } else {
                repeat(nextRoll) {
                    position2++
                    if (position2 > 10) position2 = 1
                }
                if (turn == 5) { // last player 2 turn
                    score2 += position2
                }
            }
            turn = (turn + 1) % 6
            return Universe(position1, position2, score1, score2, turn)
        }

        val universeCache = mutableMapOf<Universe, Pair<Long, Long>>()
        fun play(universe: Universe): Pair<Long, Long> {
            if (universeCache.containsKey(universe)) {
                return universeCache.getValue(universe)
            }
            if (universe.turn == 3 && universe.score1 >= 21) {
                return 1L to 0L
            } else if (universe.turn == 0 && universe.score2 >= 21) {
                return 0L to 1L
            }

            val (firstWin1, secondWin1) = play(roll(universe, 1))
            val (firstWin2, secondWin2) = play(roll(universe, 2))
            val (firstWin3, secondWin3) = play(roll(universe, 3))

            val wins = firstWin1 + firstWin2 + firstWin3 to secondWin1 + secondWin2 + secondWin3
            universeCache[universe] = wins
            return wins
        }

        val initialUniverse = Universe(input.first, input.second, 0, 0, 0)

        val (firstWin, secondWin) = play(initialUniverse)
        return maxOf(firstWin, secondWin)
    }

    val example = readInput("example/21.txt").let(::map)
    val input = readInput("21.txt").let(::map)

    assert(part01(example) == 739785L) { "got ${part01(example)}; expected 739785L" }
    println("Input: ${part01(input)}")

    assert(part02(example) == 444356092776315L) { "got ${part02(example)}; expected 444356092776315L" }
    println("Input: ${part02(input)}")
}