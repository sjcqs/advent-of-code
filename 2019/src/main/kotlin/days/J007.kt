package days

import computer.Computer
import computer.IoComputer
import computer.parseIntCodes
import kotlinx.coroutines.*

object J007 {
    val INPUT =
        "3,8,1001,8,10,8,105,1,0,0,21,38,47,64,89,110,191,272,353,434,99999,3,9,101,4,9,9,102,3,9,9,101,5,9,9,4,9,99,3,9,1002,9,5,9,4,9,99,3,9,101,2,9,9,102,5,9,9,1001,9,5,9,4,9,99,3,9,1001,9,5,9,102,4,9,9,1001,9,5,9,1002,9,2,9,1001,9,3,9,4,9,99,3,9,102,2,9,9,101,4,9,9,1002,9,4,9,1001,9,4,9,4,9,99,3,9,101,1,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,2,9,9,4,9,99,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,99,3,9,1001,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,101,1,9,9,4,9,99,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,99,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,99"

    enum class Mode {
        SIMPLE, LOOP
    }

    fun findMaxOrder(input: String, mode: Mode): Int {
        val intCodes = parseIntCodes(input)

        return runBlocking {
            when (mode) {
                Mode.SIMPLE -> runInSimpleMode(intCodes)
                Mode.LOOP -> runInLoopMode(intCodes)
            }
        }
    }

    private suspend fun runInLoopMode(intCodes: IntArray): Int {
        val sequences = allSequences((5..9))


        val array = intCodes.map { it.toLong() }.toLongArray()
        return runAmplifierLoop(listOf(9, 8, 7, 6, 5), array)
        /*return withContext(Dispatchers.Default) {
            sequences.map { codes -> runAmplifierLoop(codes, intCodes.copyOf()) }.max() ?: -1
        }*/
    }

    private fun runAmplifierLoop(codes: List<Int>, intCodes: LongArray): Int {
        val inputStream = mutableListOf<Long>()
        var previousOutputChannel = inputStream
        val computers = codes.mapIndexed { i, code ->
            println("Create computer $i")
            previousOutputChannel.add(code.toLong())
            IoComputer(i.toLong(), previousOutputChannel)
                .also { previousOutputChannel = it.output }
        }
        computers.last().output  = inputStream
        inputStream.add(0)

        var haltedCount = 0
        computers.map {
            Thread {
                runBlocking {
                    it.run(intCodes.copyOf())
                }
            }
        }.onEach { it.start() }
        while (computers.any { !it.isFinished });
        return previousOutputChannel.last().toInt()
    }

    private suspend fun runInSimpleMode(intCodes: IntArray): Int {
        val sequences = allSequences((0..4))

        return withContext(Dispatchers.Default) {
            val result = sequences.map { codes ->
                async {
                    codes.fold(0) { output, code ->
                        val outputs = Computer.run(intCodes.copyOf(), intArrayOf(code, output))
                        outputs.map { it.toInt() }.last()
                    }
                }
            }.awaitAll()

            result.max() ?: -1
        }
    }

    private fun allSequences(range: IntRange): List<List<Int>> {
        val items = range.toList()
        return range.flatMap { firstElement ->
            items
                .minus(firstElement)
                .flatMap { secondElement ->
                    items
                        .minus(firstElement)
                        .minus(secondElement)
                        .flatMap { thirdElement ->
                            items
                                .minus(firstElement)
                                .minus(secondElement)
                                .minus(thirdElement)
                                .map { fourthElement ->
                                    val fifthElement = items
                                        .minus(firstElement)
                                        .minus(secondElement)
                                        .minus(thirdElement)
                                        .minus(fourthElement)
                                        .first()
                                    listOf(firstElement, secondElement, thirdElement, fourthElement, fifthElement)
                                }
                        }
                }
        }
    }
}