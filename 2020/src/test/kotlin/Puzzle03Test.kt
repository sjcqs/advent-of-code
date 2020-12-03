import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class Puzzle03Test {

    @ParameterizedTest
    @MethodSource("stream01")
    fun run01(input: List<String>, expected: String) {
        assertEquals(expected, Puzzle03Part01.run(input))
    }

    @ParameterizedTest
    @MethodSource("stream02")
    fun run02(input: List<String>, expected: String) {
        assertEquals(expected, Puzzle03Part02.run(input))
    }


    companion object {
        @JvmStatic
        fun stream01() = Stream.of(
            listOf(
                "..##.......",
                "#...#...#..",
                ".#....#..#.",
                "..#.#...#.#",
                ".#...##..#.",
                "..#.##.....",
                ".#.#.#....#",
                ".#........#",
                "#.##...#...",
                "#...##....#",
                ".#..#...#.#",
            )
                gives "7"
        )

        @JvmStatic
        fun stream02() = Stream.of(
            listOf(
                "..##.......",
                "#...#...#..",
                ".#....#..#.",
                "..#.#...#.#",
                ".#...##..#.",
                "..#.##.....",
                ".#.#.#....#",
                ".#........#",
                "#.##...#...",
                "#...##....#",
                ".#..#...#.#",
            )
                gives "336"
        )
    }
}