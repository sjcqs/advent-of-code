import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import puzzle18.Puzzle18
import java.util.stream.Stream

internal class Puzzle18Test {

    @ParameterizedTest
    @MethodSource("stream01")
    fun run01(input: String, expected: Puzzle18.Output) {
        assertEquals(expected, Puzzle18.runBlocking(input, true))
    }


    companion object {

        @JvmStatic
        fun stream01() = Stream.of(
            Arguments.of("Puzzle18/05.txt", Puzzle18.Output(71, 231)),
            Arguments.of("Puzzle18/06.txt", Puzzle18.Output(51, 51)),
            Arguments.of("Puzzle18/01.txt", Puzzle18.Output(26, 46)),
            Arguments.of("Puzzle18/02.txt", Puzzle18.Output(437, 1445)),
            Arguments.of("Puzzle18/03.txt", Puzzle18.Output(12240, 669060)),
            Arguments.of("Puzzle18/04.txt", Puzzle18.Output(13632, 23340)),
        )
    }
}
