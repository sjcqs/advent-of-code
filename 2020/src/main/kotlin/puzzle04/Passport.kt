package puzzle04

data class Passport(val fields: List<Field>) {
    fun containsAllValidRequiredFields(): Boolean {
        val containAllRequiredFields = containAllRequiredFields()
        val areAllFieldsValid = areAllFieldsValid()
        return containAllRequiredFields && areAllFieldsValid
    }

    private fun areAllFieldsValid() = fields.all(Field::isValid)

    fun containAllRequiredFields() = fields.map(Field::type).containsAll(Field.REQUIRED_TYPES)
}

sealed class Field(val type: Type) {
    abstract val value: String
    abstract val isValid: Boolean
    data class BirthYear(override val value: String) : Field(Type.BirthYear) {
        override val isValid: Boolean
            get() {
                val year = value.toIntOrNull()
                return YEAR_PATTERN.matches(value) && year in 1920..2002
            }
    }
    data class IssueYear(override val value: String) : Field(Type.IssueYear) {
        override val isValid: Boolean
            get() = YEAR_PATTERN.matches(value) && value.toIntOrNull() in 2010..2020
    }
    data class ExpirationYear(override val value: String) : Field(Type.ExpirationYear) {
        override val isValid: Boolean
            get() = YEAR_PATTERN.matches(value) && value.toIntOrNull() in 2020..2030
    }
    data class Height(override val value: String) : Field(Type.Height) {
        override val isValid: Boolean
            get() = HEIGHT_PATTERN.matchEntire(value)?.let { result ->
                val (height, unit) = result.destructured
                when (unit) {
                    "cm" -> height.toIntOrNull() in 150..193
                    "in" -> height.toIntOrNull() in 59..76
                    else -> false
                }
            } ?: false
    }
    data class HairColor(override val value: String) : Field(Type.HairColor) {
        override val isValid: Boolean
            get() = COLOR_PATTERN.matches(value)
    }
    data class EyeColor(override val value: String) : Field(Type.EyeColor) {
        override val isValid: Boolean
            get() = EYE_COLOR_PATTERN.matches(value)
    }
    data class PassportID(override val value: String) : Field(Type.PassportID) {
        override val isValid: Boolean
            get() = PASSPORT_ID_PATTERN.matches(value)
    }
    data class CountryID(override val value: String) : Field(Type.CountryID) {
        override val isValid: Boolean = true
    }
    data class Unknown(override val value: String) : Field(Type.Unknown) {
        override val isValid: Boolean = true
    }

    enum class Type(val id: String, val isOptional: Boolean) {
        BirthYear("byr", false),
        IssueYear("iyr", false),
        ExpirationYear("eyr", false),
        Height("hgt", false),
        HairColor("hcl", false),
        EyeColor("ecl", false),
        PassportID("pid", false),
        CountryID("cid", true),
        Unknown("", true);
    }

    companion object {
        private val YEAR_PATTERN = """\d{4}""".toRegex()
        private val HEIGHT_PATTERN = """(\d+)(in|cm)""".toRegex()
        private val COLOR_PATTERN = """#(\d|[a-f]){6}""".toRegex()
        private val EYE_COLOR_PATTERN = """amb|blu|brn|gry|grn|hzl|oth""".toRegex()
        private val PASSPORT_ID_PATTERN = """\d{9}""".toRegex()
        val REQUIRED_TYPES = Type.values().filter { !it.isOptional }
    }
}