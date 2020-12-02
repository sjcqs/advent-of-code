interface Puzzle {
    val inputFileName: String
    fun run(input: List<String>): String
}