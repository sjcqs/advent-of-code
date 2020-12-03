object Puzzle03Part01 : Puzzle {
    private data class Slope(val dX: Int, val dY: Int)

    private const val TREE_SYMBOL = '#'
    private val DEFAULT_SLOPE = Slope(3, 1)

    override val inputFileName: String
        get() = "Puzzle03.txt"

    override fun run(input: List<String>): String {
        val pattern = input.map { row ->
            row.map { it == TREE_SYMBOL }
        }
        return pattern.filterIndexed { y, _ ->
            y % DEFAULT_SLOPE.dY == 0
        }.mapIndexed { index, row ->
            val x = index * DEFAULT_SLOPE.dX
            row[x % row.size]
        }.count { it }
            .toString()
    }
}