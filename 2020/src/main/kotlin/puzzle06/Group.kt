package puzzle06

import InputMapper
import split

data class Group(
    val questionsIds: Set<Char>,
    val answers: List<Answers>
) {
    val affirmativeAnswers: Int = answers.flatMap { personAnswers ->
        personAnswers.answers
    }.distinct().size

    val agreedAnswers: Int = answers
        .map(Answers::answers)
        .reduce { groupPositiveAnswerIds, personPositiveAnswerIds ->
            groupPositiveAnswerIds.intersect(personPositiveAnswerIds)
        }.size

    companion object {
        val MAPPER = InputMapper { content ->
            content.split("\n\n")
                .map(::from)
        }

        fun from(value: String): Group {
            val questionIds = value.toList()
                .filter(Char::isLetter)
                .toSet()
            val answers = value.split()
                .mapIndexed { index, answers ->
                    Answers(index, answers.toSet())
                }
            return Group(questionIds, answers)
        }
    }
}

data class Answers(
    val personId: Int,
    val answers: Set<Char>
) : Set<Char> by answers
