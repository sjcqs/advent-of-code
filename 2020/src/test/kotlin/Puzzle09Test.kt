import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import puzzle08.Puzzle08Part02
import puzzle09.Puzzle09
import java.util.stream.Stream

internal class Puzzle09Test {

    @ParameterizedTest
    @MethodSource("stream01")
    fun run01(input: String, windowSize: Int, expected: Puzzle09.Output) {
        assertEquals(expected, Puzzle09(windowSize).runBlocking(input))
    }


    companion object {

        @JvmStatic
        fun stream01() = Stream.of(
            Arguments.of(
                "Puzzle09/001.txt", 5, Puzzle09.Output(127, 62)
            )
        )
    }
}
