import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import puzzle02.Puzzle02Part01
import puzzle02.Puzzle02Part02
import java.util.stream.Stream

internal class Puzzle02Test {

    @ParameterizedTest
    @MethodSource("stream01")
    fun run01(input: String, expected: Int) {
        assertEquals(expected, Puzzle02Part01.runBlocking(input))
    }

    @ParameterizedTest
    @MethodSource("stream02")
    fun run02(input: String, expected: Int) {
        assertEquals(expected, Puzzle02Part02.runBlocking(input))
    }


    companion object {
        @JvmStatic
        fun stream01() = Stream.of("Puzzle02/001.txt" gives 2)

        @JvmStatic
        fun stream02() = Stream.of("Puzzle02/001.txt" gives 1)
    }

}