package puzzle05

import Puzzle

object Puzzle05Part02 : Puzzle<List<Seat>, Int>(mapper) {
    override val inputFileName: String = "05.txt"

    private val ROWS = 0..127
    private val COLUMNS = 0..7

    override suspend fun doJob(input: List<Seat>): Int {
        val allSeatIds = ROWS.flatMap { row ->
            COLUMNS.map { column ->
                Seat(row, column).id
            }
        }
        val usedSeatIds = input.map(Seat::id)
        val missingSeatIds = (allSeatIds - usedSeatIds)

        return missingSeatIds.find { seatId ->
            usedSeatIds.contains(seatId - 1) && usedSeatIds.contains(seatId + 1)
        } ?: -1
    }
}