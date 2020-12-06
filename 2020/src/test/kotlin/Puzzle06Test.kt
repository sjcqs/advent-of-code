import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import puzzle04.Puzzle04Part02
import puzzle06.Answers
import puzzle06.Group
import puzzle06.Puzzle06Part01
import puzzle06.Puzzle06Part02
import java.util.stream.Stream

internal class Puzzle06Test {

    @ParameterizedTest
    @MethodSource("groups")
    fun mapGroups(
        input: String,
        expected: Group,
        affirmativeAnswers: Int,
        agreedAnswers: Int
    ) {
        val group = Group.from(input)
        assertEquals(expected, group)
        assertEquals(affirmativeAnswers, group.affirmativeAnswers)
        assertEquals(agreedAnswers, group.agreedAnswers)
    }

    @ParameterizedTest
    @MethodSource("stream01")
    fun run01(input: String, expected: Int) {
        assertEquals(expected, Puzzle06Part01.runBlocking(input))
    }

    @ParameterizedTest
    @MethodSource("stream02")
    fun run02(input: String, expected: Int) {
        assertEquals(expected, Puzzle06Part02.runBlocking(input))
    }


    companion object {
        @JvmStatic
        fun groups() = Stream.of(
            Arguments.of(
                "abc",
                Group(
                    listOf(
                        Answers(
                            0, mapOf(
                                'a' to true,
                                'b' to true,
                                'c' to true,
                            )
                        )
                    )
                ),
                3,
                3
            ),
            Arguments.of(
                "b", Group(
                    listOf(
                        Answers(0, mapOf('b' to true))
                    )
                ),
                1,
                1
            ),
            Arguments.of(
                """
                a
                b
                c
                """.trimIndent(),
                Group(
                    listOf(
                        Answers(0, mapOf('a' to true, 'b' to false, 'c' to false)),
                        Answers(1, mapOf('a' to false, 'b' to true, 'c' to false)),
                        Answers(2, mapOf('a' to false, 'b' to false, 'c' to true)),
                    )
                ),
                3,
                0
            ),
            Arguments.of(
                """
                ab
                ac
                """.trimIndent(),
                Group(
                    listOf(
                        Answers(0, mapOf('a' to true, 'b' to true, 'c' to false)),
                        Answers(1, mapOf('a' to true, 'b' to false, 'c' to true)),
                    )
                ),
                3,
                1
            ),
            Arguments.of(
                """
                a
                a
                a
                a
                """.trimIndent(),
                Group(
                    listOf(
                        Answers(0, mapOf('a' to true)),
                        Answers(1, mapOf('a' to true)),
                        Answers(2, mapOf('a' to true)),
                        Answers(3, mapOf('a' to true)),
                    )
                ),
                1,
                1
            )
        )

        @JvmStatic
        fun stream01() = Stream.of("Puzzle06/001.txt" gives 11)

        @JvmStatic
        fun stream02() = Stream.of(
            "Puzzle06/001.txt" gives 6
        )
    }
}
