package puzzle07

import Puzzle

object Puzzle07Part02 : Puzzle<Rules, Int>(DAY07_MAPPER) {
    override val inputFileName: String = "07.txt"
    private const val SEARCHED_BAG = "shiny gold"

    override suspend fun doJob(input: Rules): Int {
        return count(SEARCHED_BAG, input) - 1
    }

    private fun count(bag: String, hold: Map<String, Set<BagRule>>): Int {
        val bagsInside = hold[bag] ?: emptySet()

        return 1 + bagsInside.map {
            it.count * count(it.color, hold)
        }.sum()
    }
}