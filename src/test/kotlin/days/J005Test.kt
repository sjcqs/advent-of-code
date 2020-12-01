package days

import computer.IoComputer
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class J005Test {
    @ParameterizedTest
    @MethodSource("instructions")
    internal fun testComputer(args: IntArray, input: Int, expected: List<Int>) {
        assertArrayEquals(expected.toTypedArray(), J005.run(args, input).map { it.toInt() }.toTypedArray())
    }

    @ParameterizedTest
    @MethodSource("instructions")
    internal fun testIoComputer(args: IntArray, input: Int, expected: List<Int>) {
        runBlocking {
            val inputChannel = mutableListOf(input.toLong())
            val output = mutableListOf<Long>()

            val computer = IoComputer(0,inputChannel, output)
            computer.run(args.map {it.toLong()}.toLongArray())
            assertArrayEquals(expected.toTypedArray(), output.toList().toTypedArray())
        }
    }

    companion object {
        @JvmStatic
        fun instructions() = Stream.of(
            Arguments.of(intArrayOf(3, 0, 4, 0, 99), 1, listOf(1)),
            Arguments.of(intArrayOf(103, 0, 4, 0, 99), 2, listOf(2)),
            Arguments.of(intArrayOf(3,9,8,9,10,9,4,9,99,-1,8), 8, listOf(1)),
            Arguments.of(intArrayOf(3,9,8,9,10,9,4,9,99,-1,8), 7, listOf(0)),
            Arguments.of(intArrayOf(3,9,7,9,10,9,4,9,99,-1,8), 2, listOf(1)),
            Arguments.of(intArrayOf(3,9,7,9,10,9,4,9,99,-1,8), 9, listOf(0)),
            Arguments.of(intArrayOf(3,3,1108,-1,8,3,4,3,99), 2, listOf(0)),
            Arguments.of(intArrayOf(3,3,1108,-1,8,3,4,3,99), 8, listOf(1)),
            Arguments.of(intArrayOf(3,3,1107,-1,8,3,4,3,99), 2, listOf(1)),
            Arguments.of(intArrayOf(3,3,1107,-1,8,3,4,3,99), 8, listOf(0)),
            Arguments.of(intArrayOf(3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9), 0, listOf(0)),
            Arguments.of(intArrayOf(3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9), 4, listOf(1)),
            Arguments.of(intArrayOf(3,3,1105,-1,9,1101,0,0,12,4,12,99,1), 0, listOf(0)),
            Arguments.of(intArrayOf(3,3,1105,-1,9,1101,0,0,12,4,12,99,1), 4, listOf(1)),
            Arguments.of(intArrayOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31, 1106,0, 36,98,0,0,1002,21,125,20,4,20,1105,1,46,104, 999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99), 4, listOf(999)),
            Arguments.of(intArrayOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31, 1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104, 999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99), 8, listOf(1000)),
            Arguments.of(intArrayOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31, 1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104, 999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99), 9, listOf(1001)),
            Arguments.of(intArrayOf(1101, 100, -1, 4, 0), 1, listOf<String>())
        )
    }
}