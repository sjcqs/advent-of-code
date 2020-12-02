

object Puzzle01Part02 : Puzzle {
    override val inputFileName: String = "Puzzle01.txt"
    private const val EXPECTED_SUM = 2020
    override fun run(input: List<String>): String {
        val report = input.map(String::toInt)
        for (x in report) {
            for (y in report) {
                for (z in report) {
                    if (x + y + z == EXPECTED_SUM) return "${x * y * z}"
                }
            }
        }
        return ""
    }
}