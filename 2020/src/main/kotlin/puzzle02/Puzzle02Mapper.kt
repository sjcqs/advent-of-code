package puzzle02

import InputMapper
import split

object Puzzle02Mapper : InputMapper<List<Entry>> {
    private val REGEX = """(\d+-\d+ \w): (.*)""".toRegex()

    override fun map(input: String): List<Entry> {
        return input.split().mapNotNull { entry ->
            REGEX.matchEntire(entry)?.let { result ->
                val (policyFormat, password) = result.destructured
                Entry(PasswordPolicy(policyFormat), password)
            }
        }
    }
}