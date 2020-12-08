import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import puzzle06.Puzzle06Part02
import puzzle07.Puzzle07Part01
import puzzle07.Puzzle07Part02
import puzzle08.Puzzle08Part01
import puzzle08.Puzzle08Part02
import java.util.stream.Stream

internal class Puzzle08Test {

    @ParameterizedTest
    @MethodSource("stream01")
    fun run01(input: String, expected: Int) {
        assertEquals(expected, Puzzle08Part01.runBlocking(input))
    }

    @ParameterizedTest
    @MethodSource("stream02")
    fun run02(input: String, expected: Int) {
        assertEquals(expected, Puzzle08Part02.runBlocking(input))
    }


    companion object {

        @JvmStatic
        fun stream01() = Stream.of("Puzzle08/001.txt" gives 5)

        @JvmStatic
        fun stream02() = Stream.of(
            "Puzzle08/001.txt" gives 8
        )
    }
}
