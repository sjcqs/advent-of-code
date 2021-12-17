package puzzle16

import readInput

private const val TYPE_LENGTH = 3
private const val LENGTH_TYPE_LENGTH = 1
private const val LITERAL_LENGTH_TYPE_LENGTH = 15
private const val COUNT_LENGTH_TYPE_LENGTH = 11
private const val LITERAL_CHUNK_LENGTH = 5
private const val VERSION_LENGTH = 3

private sealed interface Packet {
    val rawData: String
    val version: Int
    val packets: List<Packet>
    val range: IntRange
    val value: Long

    data class Literal(
        override val rawData: String,
        override val value: Long,
        override val version: Int,
        override val range: IntRange
    ) : Packet {
        override val packets = emptyList<Packet>()
    }

    sealed interface Operator : Packet {
        companion object {
            operator fun invoke(
                rawData: String,
                version: Int,
                type: Int,
                subpackets: List<Packet>,
                range: IntRange
            ): Operator = when (type) {
                0 -> Sum(rawData, version, subpackets, range)
                1 -> Product(rawData, version, subpackets, range)
                2 -> Minimum(rawData, version, subpackets, range)
                3 -> Maximum(rawData, version, subpackets, range)
                5 -> GreaterThan(rawData, version, subpackets, range)
                6 -> LessThan(rawData, version, subpackets, range)
                7 -> EqualTo(rawData, version, subpackets, range)
                else -> error("invalid type: $type")
            }
        }
    }

    data class Sum(
        override val rawData: String,
        override val version: Int,
        override val packets: List<Packet>,
        override val range: IntRange
    ) : Operator {
        override val value: Long
            get() = packets.sumOf { it.value }
    }

    data class Product(
        override val rawData: String,
        override val version: Int,
        override val packets: List<Packet>,
        override val range: IntRange
    ) : Operator {
        override val value: Long
            get() = packets.fold(1L) { acc, packet -> acc * packet.value }
    }

    data class Minimum(
        override val rawData: String,
        override val version: Int,
        override val packets: List<Packet>,
        override val range: IntRange
    ) : Operator {
        override val value: Long
            get() = packets.minOf { it.value }
    }

    data class Maximum(
        override val rawData: String,
        override val version: Int,
        override val packets: List<Packet>,
        override val range: IntRange
    ) : Operator {
        override val value: Long
            get() = packets.maxOf { it.value }
    }

    data class GreaterThan(
        override val rawData: String,
        override val version: Int,
        override val packets: List<Packet>,
        override val range: IntRange
    ) : Operator {
        override val value: Long
            get() {
                val (left, right) = packets
                return if (left.value > right.value) 1 else 0
            }
    }

    data class LessThan(
        override val rawData: String,
        override val version: Int,
        override val packets: List<Packet>,
        override val range: IntRange
    ) : Operator {
        override val value: Long
            get() {
                val (left, right) = packets
                return if (left.value < right.value) 1 else 0
            }
    }

    data class EqualTo(
        override val rawData: String,
        override val version: Int,
        override val packets: List<Packet>,
        override val range: IntRange
    ) : Operator {
        override val value: Long
            get() {
                val (left, right) = packets
                return if (left.value == right.value) 1 else 0
            }
    }
}

fun main() {

    fun map(input: String): String {
        return input.map { hex ->
            hex.digitToInt(16).toString(2).let { digit ->
                "0".repeat(4 - digit.length) + digit
            }
        }.joinToString("")
    }

    fun parse(packet: String, start: Int = 0): Packet = with(packet) {
        var cursor = 0
        val version = drop(cursor).take(VERSION_LENGTH)
        cursor += VERSION_LENGTH
        val type = drop(cursor).take(TYPE_LENGTH)
        cursor += TYPE_LENGTH

        when (type.toInt(2)) {
            4 -> {
                drop(cursor).chunked(LITERAL_CHUNK_LENGTH)
                    .foldIndexed("" to -1) { index, (literal, lastIndex), chunk ->
                        if (lastIndex < 0) {
                            when (chunk.first()) {
                                '1' -> literal + chunk.drop(1) to -1
                                '0' -> literal + chunk.drop(1) to (index + 1) * LITERAL_CHUNK_LENGTH
                                else -> literal to lastIndex
                            }
                        } else {
                            literal to lastIndex
                        }
                    }.let { (literal, index) ->
                        val endOfPacket = 6 + index
                        Packet.Literal(
                            rawData = packet.take(endOfPacket),
                            value = literal.toLong(2),
                            version = version.toInt(2),
                            range = start..start + endOfPacket
                        )
                    }
            }
            else -> {
                val lengthType = drop(cursor).take(LENGTH_TYPE_LENGTH).toInt(2)
                cursor += LENGTH_TYPE_LENGTH
                when (lengthType) {
                    0 -> {
                        val length = drop(cursor).take(LITERAL_LENGTH_TYPE_LENGTH).toInt(2)
                        cursor += LITERAL_LENGTH_TYPE_LENGTH
                        var consumed = 0
                        val subpackets = mutableListOf<Packet>()
                        while (consumed < length) {
                            val subpacket = parse(drop(cursor), cursor)
                            val range = subpacket.range
                            cursor += range.last - range.first
                            consumed += range.last - range.first
                            subpackets.add(subpacket)
                        }
                        Packet.Operator(
                            rawData = packet.take(cursor),
                            version = version.toInt(2),
                            type = type.toInt(2),
                            subpackets = subpackets,
                            range = 0..cursor
                        )
                    }
                    1 -> {
                        val count = drop(cursor).take(COUNT_LENGTH_TYPE_LENGTH).toInt(2)
                        cursor += COUNT_LENGTH_TYPE_LENGTH
                        val subpackets = mutableListOf<Packet>()
                        repeat(count) {
                            val subpacket = parse(drop(cursor), cursor)
                            subpackets.add(subpacket)
                            val range = subpacket.range
                            cursor += range.last - range.first
                        }
                        Packet.Operator(
                            rawData = packet.take(cursor),
                            version = version.toInt(2),
                            type = type.toInt(2),
                            subpackets = subpackets,
                            range = 0..cursor
                        )
                    }
                    else -> error("invalid type: $lengthType")
                }
            }
        }
    }

    fun Packet.sumOfVersion(): Int = version + packets.sumOf { it.sumOfVersion() }

    fun part01(input: String): Int {
        return parse(input).sumOfVersion()
    }


    val input = readInput("16.txt").let(::map)
    val examples = mapOf(
        "D2FE28" to 6,
        "38006F45291200" to 9,
        "8A004A801A8002F478" to 16,
        "620080001611562C8802118E34" to 12,
        "C0015000016115A2E0802F182340" to 23,
        "A0016C880162017C3686B18A3D4780" to 31,
    ).mapKeys { (data, _) -> map(data) }

    examples.entries.forEachIndexed { index, (example, expected) ->
        assert(part01(example) == expected) { "expected: $expected, got ${part01(example)}" }
    }
    println("Part 01: ${part01(input)}")
    mapOf(
        "C200B40A82" to 3L,
        "04005AC33890" to 54L,
        "880086C3E88112" to 7L,
        "CE00C43D881120" to 9L,
        "D8005AC2A8F0" to 1L,
        "F600BC2D8F" to 0L,
        "9C005AC2F8F0" to 0L,
        "9C0141080250320F1802104A08" to 1L,
    ).mapKeys { (data, _) -> map(data) }
        .forEach { (example, expected) ->
            assert(parse(example).value == expected) { "expected: $expected, got ${parse(example).value}" }
        }
    println("Part 02: ${parse(input).value}")
}