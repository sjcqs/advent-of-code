import computer.parseIntCodes
import days.*

fun main(args: Array<String>) {
    println("J001")
    J001.run(J001.INPUT.split("\n").toTypedArray())
    println("----------------------")
    println("J002")
    val j002Array = parseIntCodes(J002.INPUT)
    J002.run(j002Array.copyOf().also { it[1] = 12; it[2] = 2 }).also {
        println(it.joinToString())
    }
    J002.findVerb(j002Array.copyOf(), 19690720).also {
        println(it.joinToString())
    }
    println("----------------------")
    println("J003")
    val distance = J003.minDistance(J003.INPUT)
    println("Min manhattan distance: $distance")
    val steps = J003.minSteps(J003.INPUT)
    println("Min steps: $steps")

    println("----------------------")
    println("J004")
    println(J004.possiblePasswordsWithin(J004.INPUT))
    println(J004.realPossiblePasswordsCountWithin(J004.INPUT))

    println("----------------------")
    println("J005")
    val j005Args = J005.INPUT.split(",").map { it.toInt() }.toIntArray()
    println(J005.run(j005Args.copyOf(), 1))
    println()
    println("PART 2")
    println(J005.run(j005Args.copyOf(), 5))

    println("----------------------")
    println("J006")
    val j006input = loadResource("j006.txt")
    println(J006.orbitsCountChecksum(j006input))
    println(J006.minimumOrbitalTransfersCount(j006input, "YOU", "SAN", false))

    println("----------------------")
    println("J007")
    println("Max: ${J007.findMaxOrder(J007.INPUT, J007.Mode.SIMPLE)}")

}