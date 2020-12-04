import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import puzzle04.Puzzle04Part01
import puzzle04.Puzzle04Part02
import java.util.stream.Stream

internal class Puzzle04Test {

    @ParameterizedTest
    @MethodSource("stream01")
    fun run01(input: String, expected: Int) {
        assertEquals(expected, Puzzle04Part01.runBlocking(input))
    }

    @ParameterizedTest
    @MethodSource("stream02")
    fun run02(input: String, expected: Int) {
        assertEquals(expected, Puzzle04Part02.runBlocking(input))
    }


    companion object {
        @JvmStatic
        fun stream01() = Stream.of("Puzzle04/001.txt" gives 2)

        @JvmStatic
        fun stream02() = Stream.of(
            "Puzzle04/002.txt" gives 0,
            "Puzzle04/003.txt" gives 4,
            "Puzzle04/004.txt" gives 4
        )
    }
}
