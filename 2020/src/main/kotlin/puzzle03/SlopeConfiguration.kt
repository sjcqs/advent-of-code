package puzzle03

data class SlopeConfiguration(
    val slopes: List<Slope>,
    val pattern: Pattern
) {
    fun encounteredTreeProduct(): Long {
        return slopes.map(pattern::encounteredTree)
            .foldRight(1L) { total, current -> total * current }
    }
}