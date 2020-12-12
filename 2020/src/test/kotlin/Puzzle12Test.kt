import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import puzzle12.Puzzle12
import java.util.stream.Stream

internal class Puzzle12Test {

    @ParameterizedTest
    @MethodSource("stream01")
    fun run01(input: String, expected: Puzzle12.Output) {
        assertEquals(expected, Puzzle12.runBlocking(input))
    }


    companion object {

        @JvmStatic
        fun stream01() = Stream.of(
            "Puzzle12/001.txt" gives Puzzle12.Output(25, 286),
            "Puzzle12/002.txt" gives Puzzle12.Output(23, 274),
            "Puzzle12/003.txt" gives Puzzle12.Output(10, 110),
            "Puzzle12/004.txt" gives Puzzle12.Output(10, 110),
            "Puzzle12/005.txt" gives Puzzle12.Output(10, 110),
        )
    }
}
