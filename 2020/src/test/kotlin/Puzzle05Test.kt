import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import puzzle04.Puzzle04Part02
import puzzle05.Seat
import puzzle05.Puzzle05Part01
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

    @ParameterizedTest
    @MethodSource("stream02")
    fun run02(input: String, expected: Int) {
        assertEquals(expected, Puzzle04Part02.runBlocking(input))
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

        @JvmStatic
        fun stream02() = Stream.of(
            "Puzzle04/002.txt" gives 0,
            "Puzzle04/003.txt" gives 4,
            "Puzzle04/004.txt" gives 4
        )
    }
}
