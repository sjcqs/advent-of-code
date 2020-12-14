import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import puzzle14.Puzzle14
import java.util.stream.Stream

internal class Puzzle14Test {

    @ParameterizedTest
    @MethodSource("stream01")
    fun run01(input: String, expected: Puzzle14.Output) {
        assertEquals(expected, Puzzle14.runBlocking(input))
    }


    companion object {

        @JvmStatic
        fun stream01() = Stream.of(
            "Puzzle14/002.txt" gives Puzzle14.Output(51, 208),
            "Puzzle14/001.txt" gives Puzzle14.Output(165, 0),
        )
    }
}
