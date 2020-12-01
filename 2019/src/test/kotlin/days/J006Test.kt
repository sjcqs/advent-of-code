package days

import gives
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class J006Test {
    @ParameterizedTest
    @MethodSource("checksum")
    internal fun testOrbitsChecksum(value: String, expected: Int) {
        assertEquals(expected, J006.orbitsCountChecksum(value))
    }

    @ParameterizedTest
    @MethodSource("transfers")
    internal fun testMinimumOrbitalTransfers(value: String, overkill: Boolean, expected: Int) {
        assertEquals(expected, J006.minimumOrbitalTransfersCount(value, "YOU", "SAN", overkill))
    }

    companion object {
        @JvmStatic
        fun checksum() = Stream.of(
            Arguments.of(
                "COM)B\n" +
                    "B)C\n" +
                    "C)D\n" +
                    "D)E\n" +
                    "E)F\n" +
                    "B)G\n" +
                    "G)H\n" +
                    "D)I\n" +
                    "E)J\n" +
                    "J)K\n" +
                    "K)L", 42
            )
        )

        @JvmStatic
        fun transfers() = Stream.of(
            Arguments.of("COM)B\n" +
                "B)C\n" +
                "C)D\n" +
                "D)E\n" +
                "E)F\n" +
                "B)G\n" +
                "G)H\n" +
                "D)I\n" +
                "E)J\n" +
                "J)K\n" +
                "K)L\n" +
                "K)YOU\n" +
                "I)SAN",false, 4),
            Arguments.of("COM)B\n" +
                "B)C\n" +
                "C)D\n" +
                "D)E\n" +
                "E)F\n" +
                "B)G\n" +
                "G)H\n" +
                "D)I\n" +
                "E)J\n" +
                "J)K\n" +
                "K)L\n" +
                "K)YOU\n" +
                "I)SAN",true, 4)
        )
    }
}