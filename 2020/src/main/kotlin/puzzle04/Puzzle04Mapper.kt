package puzzle04

import InputMapper

object Puzzle04Mapper : InputMapper<List<Passport>> {
    override fun map(input: String): List<Passport> {
        return input.split("\n\n").map { file ->
            val fields = file.split("\n", " ")
                .map { fieldEntry ->
                    val (key, value) = fieldEntry.split(":")
                    when (key) {
                        Field.Type.BirthYear.id -> Field.BirthYear(value)
                        Field.Type.IssueYear.id -> Field.IssueYear(value)
                        Field.Type.ExpirationYear.id -> Field.ExpirationYear(value)
                        Field.Type.Height.id -> Field.Height(value)
                        Field.Type.HairColor.id -> Field.HairColor(value)
                        Field.Type.EyeColor.id -> Field.EyeColor(value)
                        Field.Type.PassportID.id -> Field.PassportID(value)
                        Field.Type.CountryID.id -> Field.CountryID(value)
                        else -> Field.Unknown(value)
                    }
                }
            Passport(fields)
        }
    }

}