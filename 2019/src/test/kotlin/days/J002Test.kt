package days

import gives
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

import java.util.stream.Stream


internal class J002Test {

    @ParameterizedTest
    @MethodSource("computer")
    internal fun testComputer(input: IntArray, expected: IntArray) {
        assertArrayEquals(expected, J002.run(input))
    }


    companion object {
        @JvmStatic
        fun computer() = Stream.of(
            intArrayOf(1, 0, 0, 0, 99) gives intArrayOf(2, 0, 0, 0, 99),
            intArrayOf(2, 3, 0, 3, 99) gives intArrayOf(2, 3, 0, 6, 99),
            intArrayOf(2, 4, 4, 5, 99, 0) gives intArrayOf(2, 4, 4, 5, 99, 9801),
            intArrayOf(1, 1, 1, 4, 99, 5, 6, 0, 99) gives intArrayOf(30, 1, 1, 4, 2, 5, 6, 0, 99)
        )
    }
}