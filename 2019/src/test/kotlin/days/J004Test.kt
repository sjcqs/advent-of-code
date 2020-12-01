package days

import gives
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class J004Test {
    @ParameterizedTest
    @MethodSource("passwords")
    internal fun testIsValid(input: Int, expected: Boolean) {
        assertEquals(expected, J004.isValid(input))
    }

    @ParameterizedTest
    @MethodSource("passwords2")
    internal fun testIsReallyValid(input: Int, expected: Boolean) {
        assertEquals(expected, J004.isReallyValid(input))
    }

    companion object {
        @JvmStatic
        fun passwords() = Stream.of(
            11 gives true,
            111111 gives true,
            112233 gives true,
            1122334455 gives true,
            223450 gives false,
            1234789 gives false,
            1122233 gives true,
            1112345 gives true,
            0 gives false
        )

        @JvmStatic
        fun passwords2() = Stream.of(
            11 gives true,
            111111 gives false,
            112233 gives true,
            1122334455 gives true,
            223450 gives false,
            1234789 gives false,
            1122233 gives true,
            1112345 gives false,
            0 gives false
        )

    }
}