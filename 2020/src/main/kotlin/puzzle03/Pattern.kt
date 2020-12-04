package puzzle03

data class Pattern(val trees: List<List<Boolean>>) {
    fun encounteredTree(slope: Slope): Int {
        return trees.filterIndexed { y, _ ->
            y % slope.dY == 0
        }.mapIndexed { index, row ->
            val x = index * slope.dX
            row[x % row.size]
        }.count { it }
    }
}