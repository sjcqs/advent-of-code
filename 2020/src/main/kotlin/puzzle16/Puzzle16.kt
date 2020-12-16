package puzzle16

import InputMapper
import Puzzle

private val RULE_REGEX = """(.+): ((\d+-\d+) or (\d+-\d+))""".toRegex()

object Puzzle16 : Puzzle<Puzzle16.Input, Puzzle16.Output>(
    InputMapper { input ->
        fun String.toRange(): IntRange {
            val (start, end) = split("-")
            return start.toInt()..end.toInt()
        }

        fun String.toTicket(): Ticket {
            return Ticket(split(",").map { value -> Ticket.Field("", value.trim().toInt()) })
        }
        val (rules, myTicket, tickets) = input.split("\n\n")

        Input(
            rules = rules.split("\n")
                .associate { rule ->
                    val result = requireNotNull(RULE_REGEX.matchEntire(rule))
                    val (name, _, range0, range1) = result.destructured
                    name to listOf(
                        range0.toRange(),
                        range1.toRange()
                    )
                },
            myTicket = myTicket.split("\n").last().toTicket(),
            nearbyTickets = tickets.split("\n").drop(1).map(String::toTicket)
        )
    }
) {

    override val inputFileName = "16.txt"

    override suspend fun doJob(input: Input): Output {
        val ranges = input.rules.entries.flatMap { (_, ranges) -> ranges }
        val errorRate = input.nearbyTickets.sumOf { ticket ->
            ticket.fields.sumOf { field ->
                if (ranges.none { range -> field.value in range }) field.value else 0
            }
        }
        val validTickets = input.nearbyTickets.filterOutInvalid(input.rules)
        val myTicket = identifyFields(input.myTicket, validTickets, input.rules)

        val departureValues = myTicket.fields.filter { it.id.startsWith("departure") }
            .map { it.value }
        val initial: Long = if (departureValues.isEmpty()) 0 else 1
        val departureProduct = departureValues.foldRight(initial) { product, value -> product * value }
        return Output(errorRate, departureProduct)
    }

    private fun identifyFields(myTicket: Ticket, tickets: List<Ticket>, allRules: Map<String, List<IntRange>>): Ticket {
        val fieldRules = myTicket.fields.mapIndexed { index, field ->
            index to allRules.matchingRules(field)
        }.toMap(mutableMapOf())

        val foundKey = mutableListOf<String>()
        val fields = myTicket.fields.toMutableList()
        while (foundKey.size != myTicket.fields.size) {
            fieldRules.keys.forEach { index ->
                val rules = fieldRules[index] ?: error("Missing index $index")
                val nearbyFields = tickets.map { it.fields[index] }
                val matchingRules = rules.filterValues { ranges ->
                    nearbyFields.all { ranges.any { range -> it.value in range } }
                }
                val key = matchingRules.keys.first()
                if (matchingRules.size == 1 && key !in foundKey) {
                    foundKey.add(key)
                    fields[index] = fields[index].copy(id = key)
                    fieldRules.replaceAll { replacedIndex, replacedRules ->
                        if (index == replacedIndex) {
                            matchingRules
                        } else {
                            replacedRules.filterKeys { key ->
                                matchingRules.keys.first() != key
                            }
                        }
                    }
                }
            }
        }
        return Ticket(fields = fields)
    }

    private fun Map<String, List<IntRange>>.matchingRules(
        field: Ticket.Field
    ): Map<String, List<IntRange>> {
        return filterValues { ranges ->
            ranges.any { range -> field.value in range }
        }
    }

    private fun List<Ticket>.filterOutInvalid(rules: Map<String, List<IntRange>>): List<Ticket> {
        val ranges = rules.entries.flatMap { (_, ranges) -> ranges }
        return filterNot { ticket ->
            ticket.fields.any { field -> ranges.none { range -> field.value in range } }
        }
    }

    data class Input(
        val rules: Map<String, List<IntRange>>,
        val myTicket: Ticket,
        val nearbyTickets: List<Ticket>
    )

    data class Output(
        val errorRate: Int,
        val departureProduct: Long
    )
}

data class Ticket(
    val fields: List<Field>
) {
    data class Field(
        val id: String,
        val value: Int
    )
}