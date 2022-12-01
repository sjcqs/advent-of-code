import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

fun String.split() = split("\n")

fun loadResource(resource: String) = object {}.javaClass.getResource(resource).readText()
fun readInput(name: String) = File("src",name).readText()

fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)