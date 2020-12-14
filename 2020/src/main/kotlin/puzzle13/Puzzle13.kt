package puzzle13

import InputMapper
import Puzzle
import kotlin.math.ceil

object Puzzle13 : Puzzle<Puzzle13.Input, Puzzle13.Output>(
    InputMapper { input ->
        val (timestamp, ids) = input.split("\n")
        Input(
            timestamp.toLong(),
            ids.split(",")
                .map { if (it == "x") 0L else it.toLong() }
        )
    }
) {

    override val inputFileName = "13.txt"

    override suspend fun doJob(input: Input): Output {
        val earliestTimestamp = earliestTimestamp(input.ids)

        val busIdTimesWaitingMinutes = busIdTimesWaitingMinutes(input)
        return Output(
            busIdTimesWaitingMinutes,
            earliestTimestamp
        )
    }

    private fun busIdTimesWaitingMinutes(input: Input): Long {
        val earliestBus: Long? = input.ids
            .minByOrNull { delay -> Input.nextShiftAfter(delay, input.timestamp) }
        return earliestBus?.let { it * (Input.nextShiftAfter(it, input.timestamp) - input.timestamp) } ?: Long.MAX_VALUE
    }

    private fun earliestTimestamp(ids: List<Long>): Long {
        data class Bus(
            val index: Int,
            val id: Long
        )

        val busList = ids.mapIndexedNotNull { index, id ->
            if (id == 0L) null else Bus(index, id)
        }
        val steps = ids.runningFold(ids.first()) { step, id ->
            if (id == 0L) {
                step
            } else {
                lcm(step, id)
            }
        }.drop(1)
        var current = 1
        var step = busList[0].id
        var timestamp = busList.first().id
        do {
            timestamp += step
            val bus = busList[current]
            if ((timestamp + bus.index) % bus.id == 0L) {
                val nextBus = busList[current++]
                step = steps[nextBus.index]
            }
        } while (current < busList.size)
        return timestamp
    }

    private tailrec fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
    private fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b

    data class Input(
        val timestamp: Long,
        val ids: List<Long>
    ) {
        companion object {
            fun nextShiftAfter(minutes: Long, timestamp: Long): Long {
                if (minutes == 0L) return Long.MAX_VALUE
                val shift = ceil(timestamp / minutes.toDouble())

                return (shift * minutes).toLong()
            }
        }
    }

    data class Output(
        val busIdTimesWaitingMinutes: Long,
        val earliestTimestamp: Long
    )
}