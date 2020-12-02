

object Puzzle01Part01 : Puzzle {
    override val inputFileName: String = "Puzzle01.txt"
    private const val EXPECTED_SUM = 2020
    override fun run(input: List<String>): String {
        val report = input.map(String::toInt)
        for (x in report) {
            for (y in report) {
                if (x + y == EXPECTED_SUM) return "${x * y}"
            }
        }
        return ""
    }
}