package days

import gives
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class J005Test {
    @ParameterizedTest
    @MethodSource("instructions")
    internal fun testComputer(value: Pair<IntArray, Int>, expected: List<String>) {
        val (args, input) = value
        assertArrayEquals(expected.toTypedArray(), J005.run(args, input).toTypedArray())
    }

    companion object {
        @JvmStatic
        fun instructions() = Stream.of(
            Pair(intArrayOf(3, 0, 4, 0, 99), 1) gives listOf("1"),
            Pair(intArrayOf(103, 0, 4, 0, 99), 2) gives listOf("2"),
            Pair(intArrayOf(3,9,8,9,10,9,4,9,99,-1,8), 8) gives listOf("1"),
            Pair(intArrayOf(3,9,8,9,10,9,4,9,99,-1,8), 7) gives listOf("0"),
            Pair(intArrayOf(3,9,7,9,10,9,4,9,99,-1,8), 2) gives listOf("1"),
            Pair(intArrayOf(3,9,7,9,10,9,4,9,99,-1,8), 9) gives listOf("0"),
            Pair(intArrayOf(3,3,1108,-1,8,3,4,3,99), 2) gives listOf("0"),
            Pair(intArrayOf(3,3,1108,-1,8,3,4,3,99), 8) gives listOf("1"),
            Pair(intArrayOf(3,3,1107,-1,8,3,4,3,99), 2) gives listOf("1"),
            Pair(intArrayOf(3,3,1107,-1,8,3,4,3,99), 8) gives listOf("0"),
            Pair(intArrayOf(3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9), 0) gives listOf("0"),
            Pair(intArrayOf(3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9), 4) gives listOf("1"),
            Pair(intArrayOf(3,3,1105,-1,9,1101,0,0,12,4,12,99,1), 0) gives listOf("0"),
            Pair(intArrayOf(3,3,1105,-1,9,1101,0,0,12,4,12,99,1), 4) gives listOf("1"),
            Pair(intArrayOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31, 1106,0, 36,98,0,0,1002,21,125,20,4,20,1105,1,46,104, 999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99), 4) gives listOf("999"),
            Pair(intArrayOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31, 1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104, 999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99), 8) gives listOf("1000"),
            Pair(intArrayOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31, 1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104, 999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99), 9) gives listOf("1001"),
            Pair(intArrayOf(1101, 100, -1, 4, 0), 1) gives listOf<String>()
        )
    }
}