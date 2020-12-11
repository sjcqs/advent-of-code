import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import puzzle11.Puzzle11
import puzzle11.SimulationRule
import java.util.stream.Stream

internal class Puzzle11Test {

    @ParameterizedTest
    @MethodSource("stream01")
    fun run01(input: String, expected: Puzzle11.Output) {
        assertEquals(expected, Puzzle11.runBlocking(input))
    }


    companion object {

        @JvmStatic
        fun stream01() = Stream.of(
                "Puzzle11/003.txt" gives Puzzle11.Output(mapOf(SimulationRule.Adjacent to 25, SimulationRule.Visible to 9)),
                "Puzzle11/002.txt" gives Puzzle11.Output(mapOf(SimulationRule.Adjacent to 8, SimulationRule.Visible to 8)),
                "Puzzle11/001.txt" gives Puzzle11.Output(mapOf(SimulationRule.Adjacent to 37, SimulationRule.Visible to 26)),
        )
    }
}
