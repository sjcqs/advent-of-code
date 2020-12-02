object Puzzle02Part02 : Puzzle {
    override val inputFileName: String = "Puzzle02.txt"
    private val REGEX = """(\d+-\d+ \w): (.*)""".toRegex()

    private class PasswordPolicy(format: String) {
        private val letter: Char
        private val indices: Pair<Int, Int>

        init {
            val result = FORMAT_REGEX.matchEntire(format)
                ?: error("invalid format")
            val (occurrences, letter) = result.destructured

            val (start, end) = occurrences.split("-").map { it.toInt() }
            indices = Pair(start, end)
            this.letter = letter.first()
        }

        fun isValid(value: String): Boolean {
            val firstMatches = value[indices.first - 1] == letter
            val secondMatches = value[indices.second - 1] == letter
            return firstMatches xor secondMatches
        }

        companion object {
            private val FORMAT_REGEX = """(\d+-\d+) (\w)""".toRegex()
        }
    }

    override fun run(input: List<String>): String {
        return input
            .map { entry ->
                val result = REGEX.matchEntire(entry)
                if (result != null) {
                    val (policyFormat, password) = result.destructured
                    val policy = PasswordPolicy(policyFormat)
                    policy.isValid(password)
                } else {
                    false
                }
            }.count { isValid -> isValid }
            .toString()
    }
}