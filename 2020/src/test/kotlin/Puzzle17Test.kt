import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import puzzle17.Puzzle17
import java.util.stream.Stream

internal class Puzzle17Test {

    @ParameterizedTest
    @MethodSource("stream01")
    fun run01(input: String, expected: Puzzle17.Output) {
        assertEquals(expected, Puzzle17.runBlocking(input, true))
    }


    companion object {

        @JvmStatic
        fun stream01() = Stream.of(
            Arguments.of("Puzzle17/01.txt", Puzzle17.Output(112, 848))
        )
    }
}
