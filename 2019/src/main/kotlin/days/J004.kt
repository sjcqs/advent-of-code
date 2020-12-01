package days

object J004 {
    val INPUT = 246540..787419
    private val IDENTICAL_DIGITS = "1{2,}|2{2,}|3{2,}|4{2,}|5{2,}|6{2,}|7{2,}|8{2,}|9{2,}".toRegex()
    private val TWO_IDENTICAL_DIGITS_REGEX = "\\d*($IDENTICAL_DIGITS)\\d*".toRegex()

    fun possiblePasswordsWithin(input: IntRange): Int {
        return input.filter(this::isValid).count()
    }

    fun realPossiblePasswordsCountWithin(input: IntRange): Int {
        return input.filter(this::isReallyValid).count()
    }

    fun isValid(value: Int): Boolean {
        val password = value.toString()

        return TWO_IDENTICAL_DIGITS_REGEX.matches(password) &&
            password.foldIndexed(true) { index: Int, acc: Boolean, c: Char ->
                val digit = c.toString().toInt()
                acc && password
                    .take(index)
                    .all { other -> other.toString().toInt() <= digit }
            }
    }

    fun isReallyValid(value: Int): Boolean {
        val password = value.toString()
        return isValid(value) && IDENTICAL_DIGITS.findAll(password).any {
            val consequentDigits = it.value
            consequentDigits.length == 2
        }

    }
}