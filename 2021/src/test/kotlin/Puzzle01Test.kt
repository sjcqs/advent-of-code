import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import puzzle01.Puzzle01Part01
import puzzle01.Puzzle01Part02
import java.util.stream.Stream

internal class Puzzle01Test {

    @ParameterizedTest
    @MethodSource("stream01")
    fun run01(input: String, expected: Int) {
        assertEquals(expected, Puzzle01Part01.runBlocking(input))
    }

    @ParameterizedTest
    @MethodSource("stream02")
    fun run02(input: String, expected: Int) {
        assertEquals(expected, Puzzle01Part02.runBlocking(input))
    }


    companion object {
        @JvmStatic
        fun stream01() = Stream.of("Puzzle01/001.txt" gives 7)

        @JvmStatic
        fun stream02() = Stream.of("Puzzle01/001.txt" gives 5)
    }
}