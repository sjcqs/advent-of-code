import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import puzzle04.Puzzle04Part02
import puzzle05.Seat
import puzzle05.Puzzle05Part01
import puzzle05.Puzzle05Part02
import puzzle06.Puzzle06Part02
import java.util.stream.Stream

internal class Puzzle05Test {

    @ParameterizedTest
    @MethodSource("boardingPass")
    fun convertBoardingPass(input: String, expected: Seat) {
        assertEquals(expected, Seat.from(input))
    }
    @ParameterizedTest
    @MethodSource("stream01")
    fun run01(input: String, expected: Int) {
        assertEquals(expected, Puzzle05Part01.runBlocking(input))
    }


    companion object {
        @JvmStatic
        fun boardingPass() = Stream.of(
            "BFFFBBFRRR" gives Seat(567, 70,7),
            "FFFBBBFRRR" gives Seat(119, 14, 7),
            "BBFFBBFRLL" gives Seat(820, 102, 4)
        )
        @JvmStatic
        fun stream01() = Stream.of("Puzzle05/001.txt" gives 820)
    }
}
