import org.junit.jupiter.params.provider.Arguments

infix fun <T, R> T.gives(expected: R): Arguments = Arguments.arguments(this, expected)