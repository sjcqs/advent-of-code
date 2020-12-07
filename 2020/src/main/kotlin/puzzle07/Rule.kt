package puzzle07

import InputMapper

val DAY07_MAPPER = InputMapper(Rules.Companion::from)

private fun String.toColor() = replace("bags", "")
    .replace("bag", "")
    .trim()

data class BagRule(val color: String, val count: Int)

class Rules(
    val map: Map<String, Set<BagRule>>
) : Map<String, Set<BagRule>> by map {

    companion object {
        private val REGEX = """(.+) contain (.+)\.""".toRegex()
        private const val EMPTY_VALUE = "no other bags"
        private val RULE_PATTERN = """(\d+) (.+)""".toRegex()

        fun from(value: String): Rules {
            val map = value.split("\n")
                .mapNotNull { rule ->
                    REGEX.matchEntire(rule)?.let { result ->
                        val (color, bagRule) = result.destructured
                        color.toColor() to set(bagRule)
                    }
                }.toMap()
            return Rules(map)
        }

        fun set(value: String): Set<BagRule> {
            if (value == EMPTY_VALUE) return setOf()

            return value.split(",")
                .map { it.trim() }
                .map { format ->
                    val result = requireNotNull(RULE_PATTERN.matchEntire(format))
                    val (count, color) = result.destructured

                    BagRule(color.toColor(), count.toInt())
                }.toSet()
        }
    }
}