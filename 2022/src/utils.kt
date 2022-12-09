import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

fun String.split() = split("\n")

fun <T: Any> requireEquals(expected: T, actual: T) = require(expected == actual) {
    "Expected: $expected, actual: $actual"
}

data class Position(val x: Int, val y: Int)

fun loadResource(resource: String) = object {}.javaClass.getResource(resource).readText()
fun readInput(name: String) = File("src",name).readText()

fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)