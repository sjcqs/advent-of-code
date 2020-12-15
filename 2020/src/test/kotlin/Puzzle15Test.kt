import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import puzzle15.Puzzle15
import java.util.stream.Stream

internal class Puzzle15Test {

    @ParameterizedTest
    @MethodSource("stream01")
    fun run01(input: String, turns: Int, expected: Puzzle15.Output) {
        assertEquals(expected, Puzzle15(turns).runBlocking(input))
    }


    companion object {

        @JvmStatic
        fun stream01() = Stream.of(
            Arguments.of("Puzzle15/01.txt",2020,Puzzle15.Output(436)),
            Arguments.of("Puzzle15/02.txt",2020,Puzzle15.Output(1)),
            Arguments.of("Puzzle15/03.txt",2020,Puzzle15.Output(10)),
            Arguments.of("Puzzle15/04.txt",2020,Puzzle15.Output(27)),
        )
    }
}
