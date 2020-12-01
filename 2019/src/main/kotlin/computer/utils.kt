package computer

fun parseIntCodes(input: String): IntArray {
    return input.trim()
        .split(",")
        .map { it.toInt() }
        .toIntArray()
}