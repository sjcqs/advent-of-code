import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import puzzle03.Puzzle03Part01
import puzzle03.Puzzle03Part02
import java.util.stream.Stream

internal class Puzzle03Test {

    @ParameterizedTest
    @MethodSource("stream01")
    fun run01(input: String, expected: Long) {
        assertEquals(expected, Puzzle03Part01.runBlocking(input))
    }

    @ParameterizedTest
    @MethodSource("stream02")
    fun run02(input: String, expected: Long) {
        assertEquals(expected, Puzzle03Part02.runBlocking(input))
    }


    companion object {
        @JvmStatic
        fun stream01() = Stream.of("Puzzle03/001.txt" gives 7)

        @JvmStatic
        fun stream02() = Stream.of("Puzzle03/001.txt" gives 336)
    }
}
