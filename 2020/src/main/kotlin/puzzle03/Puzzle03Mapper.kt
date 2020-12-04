package puzzle03

import InputMapper
import split

class Puzzle03Mapper(private val slopes: List<Slope>) : InputMapper<SlopeConfiguration> {


    override fun map(input: String): SlopeConfiguration {
        val trees = input.split().map { row -> row.map { it == TREE_SYMBOL } }
        return SlopeConfiguration(
            slopes = slopes,
            pattern = Pattern(trees)
        )
    }

    companion object {
        private const val TREE_SYMBOL = '#'
    }
}