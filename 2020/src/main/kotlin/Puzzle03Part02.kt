object Puzzle03Part02 : Puzzle {
    private data class Slope(val dX: Int, val dY: Int)

    private const val TREE_SYMBOL = '#'
    private val SLOPES = listOf(
        Slope(1, 1),
        Slope(3, 1),
        Slope(5, 1),
        Slope(7, 1),
        Slope(1, 2),
    )

    override val inputFileName: String
        get() = "Puzzle03.txt"

    override fun run(input: List<String>): String {
        val pattern = input.map { row ->
            row.map { it == TREE_SYMBOL }
        }
        return SLOPES.map { slope ->
            encounteredTrees(pattern, slope)
        }.foldRight(1L) { total, current ->
            total.toLong() * current
        }.toString()
    }

    private fun encounteredTrees(
        pattern: List<List<Boolean>>,
        slope: Slope
    ): Int {
        return pattern.filterIndexed { y, _ ->
            y % slope.dY == 0
        }.mapIndexed { index, row ->
            val x = index * slope.dX
            row[x % row.size]
        }.count { it }
    }
}