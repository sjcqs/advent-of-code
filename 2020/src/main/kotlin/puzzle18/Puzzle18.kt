package puzzle18

import InputMapper
import Puzzle

object Puzzle18 : Puzzle<Puzzle18.Input, Puzzle18.Output>(
    InputMapper { input ->
        val leftToRight = input.split("\n")
        Input(
            leftToRight.map { Node.map(it, true) },
            leftToRight.map { Node.map(it, false) }
        )
    }
) {

    override val inputFileName = "18.txt"
    override suspend fun doJob(input: Input): Output {
        log(input.leftToRightExpressions.joinToString("\n"))
        log(input.precedenceExpressions.joinToString("\n"))
        return Output(
            input.leftToRightExpressions.sumOf { it.evaluate() }.also { log("\n") },
            input.precedenceExpressions.sumOf { it.evaluate() }
        )
    }

    data class Input(
        val leftToRightExpressions: List<Node>,
        val precedenceExpressions: List<Node>
    )

    data class Output(val sum: Long, val sumWithPrecedence: Long)

    sealed class Node {
        abstract fun evaluate(): Long
        data class Number(val value: Long) : Node() {
            override fun evaluate(): Long = value
        }

        sealed class Expression : Node() {
            data class Product(val left: Node, val right: Node) : Expression() {
                override fun evaluate(): Long {
                    val rightValue = right.evaluate()
                    val leftValue = left.evaluate()
                    return leftValue * rightValue
                }
            }

            data class Sum(val left: Node, val right: Node) : Expression() {
                override fun evaluate(): Long {
                    val rightValue = right.evaluate()
                    val leftValue = left.evaluate()
                    return leftValue + rightValue
                }
            }
        }

        companion object Mapper {

            fun map(input: String, isLeftToRight: Boolean): Node {
                log(input)
                val tokens = Token.lex(input).listIterator()
                return if (isLeftToRight) {
                    LeftToRight.expression(tokens)
                } else {
                    Precedence.expression(tokens)
                }
            }



            object LeftToRight {
                // number or parenthesis
                private fun factor(tokens: ListIterator<Token<*>>): Node {
                    return when (val token = tokens.next()) {
                        is Token.Number -> {
                            Number(token.value)
                        }
                        Token.RightParenthesis -> {
                            val node = expression(tokens)
                            assert(tokens.next() is Token.LeftParenthesis)
                            node
                        }
                        else -> error("Invalid token $token")
                    }
                }

                fun expression(tokens: ListIterator<Token<*>>): Node {
                    val node = factor(tokens)
                    if (!tokens.hasNext()) {
                        return node
                    }
                    return when (tokens.next()) {
                        is Token.Plus -> {
                            Expression.Sum(expression(tokens), node)
                        }
                        is Token.Mul -> {
                            Expression.Product(expression(tokens), node)
                        }
                        else -> {
                            tokens.previous()
                            node
                        }
                    }
                }

            }
            object Precedence {
                // number or parenthesis
                private fun term(tokens: ListIterator<Token<*>>): Node {
                    return when (val token = tokens.next()) {
                        is Token.Number -> {
                            Number(token.value)
                        }
                        Token.RightParenthesis -> {
                            val node = expression(tokens)
                            assert(tokens.next() is Token.LeftParenthesis)
                            node
                        }
                        else -> error("Invalid token $token")
                    }
                }

                private fun factor(tokens: ListIterator<Token<*>>): Node {
                    var node = term(tokens)
                    if (!tokens.hasNext()) {
                        return node
                    }
                    while (tokens.hasNext() && tokens.next() is Token.Plus) {
                        if (!tokens.hasNext()) {
                            return node
                        }
                        node = Expression.Sum(node, term(tokens))
                    }
                    tokens.previous()
                    return node
                }

                fun expression(tokens: ListIterator<Token<*>>): Node {
                    var node = factor(tokens)
                    tokens.previous()
                    if (!tokens.hasNext()) {
                        return node
                    }
                    while (tokens.hasNext() && tokens.next() is Token.Mul) {
                        if (!tokens.hasNext()) {
                            return node
                        }
                        node = Expression.Product(node, factor(tokens))
                    }
                    tokens.previous()
                    return node
                }

            }
        }

        sealed class Token<T> {
            abstract val value: T

            override fun toString(): String {
                return "$value"
            }

            data class Number(override val value: Long) : Token<Long>()
            object Plus : Token<Char>() {
                override val value: Char = '+'
            }

            object Mul : Token<Char>() {
                override val value: Char = '*'
            }

            object LeftParenthesis : Token<Char>() {
                override val value: Char = '('
            }

            object RightParenthesis : Token<Char>() {
                override val value: Char = ')'
            }

            companion object {
                fun lex(input: String): List<Token<*>> {
                    val tokens = mutableListOf<Token<*>>()
                    var index = 0
                    while (index < input.length) {
                        when (input[index]) {
                            in '0'..'9' -> {
                                val value = buildString {
                                    do {
                                        append(input[index])
                                        index++
                                    } while (index < input.length && input[index] in '0'..'9')
                                }
                                tokens.add(Number(value.toLong()))
                            }
                            '(' -> {
                                tokens.add(LeftParenthesis)
                                index++
                            }
                            ')' -> {
                                tokens.add(RightParenthesis)
                                index++
                            }
                            '+' -> {
                                tokens.add(Plus)
                                index++
                            }
                            '*' -> {

                                tokens.add(Mul)
                                index++
                            }
                            ' ' -> {
                                index++
                            }
                        }
                    }
                    log("Tokens: ${tokens.reverse()}")
                    return tokens
                }
            }
        }
    }
}