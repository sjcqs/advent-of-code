package days

import gives
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class J003Test {
    @ParameterizedTest
    @MethodSource("paths")
    internal fun testMinDistance(input: String, expected: Int) {
        assertEquals(expected, J003.minDistance(input))
    }
    @ParameterizedTest
    @MethodSource("steps")
    internal fun testSteps(input: String, expected: Int) {
        assertEquals(expected, J003.minSteps(input))
    }


    companion object {
        @JvmStatic
        fun paths() = Stream.of(
            "R8,U5,L5,D3\nU7,R6,D4,L4" gives 6,
            "R8,U5,L5,D2\nU7,R6,D4,L4" gives 6,
            "R8,U5,L4,D3\nU7,R6,D4,L4" gives 7,
            "R75,D30,R83,U83,L12,D49,R71,U7,L72\nU62,R66,U55,R34,D71,R55,D58,R83" gives 159,
            "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51\nU98,R91,D20,R16,D67,R40,U7,R15,U6,R7" gives 135
        )

        @JvmStatic
        fun steps() = Stream.of(
            "R75,D30,R83,U83,L12,D49,R71,U7,L72\nU62,R66,U55,R34,D71,R55,D58,R83" gives 610,
            "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51\nU98,R91,D20,R16,D67,R40,U7,R15,U6,R7" gives 410,
            "R8,U5,L5,D3\nU7,R6,D4,L4" gives 30
        )
    }
}