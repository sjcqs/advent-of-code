package puzzle07

import Puzzle

object Puzzle07Part01 : Puzzle<Rules, Int>(DAY07_MAPPER) {
    override val inputFileName: String = "07.txt"
    private const val SEARCHED_BAG = "shiny gold"

    override suspend fun doJob(input: Rules): Int {
        val holdBy = mutableMapOf<String, MutableSet<String>>()
        input.forEach { (key, canContainSet) ->
            canContainSet.forEach { bag ->
                holdBy
                    .getOrPut(bag.color) { mutableSetOf() }
                    .add(key)
            }
        }

        return holdsIn(SEARCHED_BAG, holdBy).size
    }

    private fun holdsIn(bag: String, holdBy: Map<String, Set<String>>): Set<String> {
        val bagHoldBy = holdBy[bag] ?: return emptySet()
        return bagHoldBy
            .map { setOf(it) + holdsIn(it, holdBy) }
            .filter { it.isNotEmpty() }
            .reduce(Set<String>::union)
    }
}