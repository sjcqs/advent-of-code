import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import puzzle10.Puzzle10
import java.util.stream.Stream

internal class Puzzle10Test {

    @ParameterizedTest
    @MethodSource("stream01")
    fun run01(input: String, expected: Puzzle10.Output) {
        assertEquals(expected, Puzzle10.runBlocking(input))
    }


    companion object {

        @JvmStatic
        fun stream01() = Stream.of(
            "Puzzle10/001.txt" gives Puzzle10.Output(220, 19208),
            "Puzzle10/002.txt" gives Puzzle10.Output(35, 8),
        )
    }
}
