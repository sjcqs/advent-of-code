package puzzle05

data class Seat(
    val id: Int,
    val row: Int,
    val column: Int
) {
    constructor(row: Int, column: Int) : this(id(row, column), row, column)

    companion object {

        fun from(value: String): Seat {
            val rowFormat = replace(value.take(7))
            val columnFormat = replace(value.takeLast(3))

            val row = convert(rowFormat)
            val column = convert(columnFormat)

            return Seat(row = row, column = column)
        }

        private fun id(row: Int, column: Int): Int {
            return row * 8 + column
        }

        private fun replace(value: String) = value.replace('F', '0')
            .replace('L', '0')
            .replace('B', '1')
            .replace('R', '1')

        private fun convert(value: String): Int {
            return Integer.parseInt(value, 2)
        }
    }
}
