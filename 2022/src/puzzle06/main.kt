package puzzle06

import readInput
import requireEquals

val examples = mapOf(
    "mjqjpqmgbljsphdztnvjfqwrcgsmlb" to 7,
    "bvwbjplbgvbhsrlpgdmjqwftvncz" to 5,
    "nppdvjthqldpwncqszvftbrmjlhg" to 6,
    "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg" to 10,
    "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw" to 11
)
val examples2 = mapOf(
    "mjqjpqmgbljsphdztnvjfqwrcgsmlb" to 19,
    "bvwbjplbgvbhsrlpgdmjqwftvncz" to 23,
    "nppdvjthqldpwncqszvftbrmjlhg" to 23,
    "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg" to 29,
    "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw" to 26
)

fun main() {
    examples.forEach { (data, expected) ->
        requireEquals(expected, distinctPacketEnd(data, 4))
    }
    println(distinctPacketEnd(readInput("06.txt"), 4))

    examples2.forEach { (data, expected) ->
        requireEquals(expected, distinctPacketEnd(data, 14))
    }
    println(distinctPacketEnd(readInput("06.txt"), 14))

}

fun distinctPacketEnd(input: String, size: Int): Int {
    return input.toList().withIndex().windowed(size) { indexedGroup ->
        (indexedGroup.last().index + 1) to indexedGroup.map { it.value }.toSet()
            .joinToString("")
    }.first { (_, group) ->
        group.length == size
    }.first
}
