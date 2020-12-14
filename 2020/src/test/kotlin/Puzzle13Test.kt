import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import puzzle13.Puzzle13
import java.util.stream.Stream

internal class Puzzle13Test {

    @ParameterizedTest
    @MethodSource("stream01")
    fun run01(input: String, expected: Puzzle13.Output) {
        assertEquals(expected, Puzzle13.runBlocking(input))
    }


    companion object {

        @JvmStatic
        fun stream01() = Stream.of(
            "Puzzle13/001.txt" gives Puzzle13.Output(0, 3417),
            "Puzzle13/002.txt" gives Puzzle13.Output(0, 754018),
            "Puzzle13/003.txt" gives Puzzle13.Output(0, 779210),
            "Puzzle13/004.txt" gives Puzzle13.Output(0, 1261476),
            "Puzzle13/005.txt" gives Puzzle13.Output(0, 1202161486),
            "Puzzle13/006.txt" gives Puzzle13.Output(0, 1068781),
        )
    }
}
