object Puzzle02Part01 : Puzzle {
    override val inputFileName: String = "Puzzle02.txt"
    private val REGEX = """(\d+-\d+ \w): (.*)""".toRegex()

    private class PasswordPolicy(format: String) {
        private val letter: Char
        private val range: IntRange

        init {
            val result = FORMAT_REGEX.matchEntire(format)
                ?: error("invalid format")
            val (occurrences, letter) = result.destructured

            val (start, end) = occurrences.split("-").map { it.toInt() }
            range = IntRange(start, end)
            this.letter = letter.first()
        }

        fun isValid(value: String): Boolean {
            val occurrences = value.count { it == letter }

            return occurrences in range
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