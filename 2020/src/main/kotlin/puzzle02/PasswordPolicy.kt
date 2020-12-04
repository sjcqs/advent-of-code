package puzzle02

class PasswordPolicy(format: String) {
    private val letter: Char
    private val range: IntRange
    private val indices: Pair<Int, Int>

    init {
        val result = FORMAT_REGEX.matchEntire(format)
            ?: error("invalid format")
        val (occurrences, letter) = result.destructured

        val (start, end) = occurrences.split("-").map { it.toInt() }
        range = IntRange(start, end)
        this.letter = letter.first()
        this.indices = Pair(start, end)
    }

    fun isValidAccordingToCount(value: String): Boolean {
        val occurrences = value.count { it == letter }

        return occurrences in range
    }

    fun isValidAccordingToIndices(value: String): Boolean {
        val firstMatches = value[indices.first - 1] == letter
        val secondMatches = value[indices.second - 1] == letter
        return firstMatches xor secondMatches
    }

    companion object {
        private val FORMAT_REGEX = """(\d+-\d+) (\w)""".toRegex()
    }
}