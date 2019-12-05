import days.*

fun main(args: Array<String>) {
    println("J001")
    J001.run(J001.INPUT.split("\n").toTypedArray())
    println("----------------------")
    println("J002")
    val j002Array = J002.INPUT.split(",").map { it.toInt() }.toIntArray()
    J002.run(j002Array.copyOf().also { it[1] = 12; it[2] = 2 }).also {
        println(it.joinToString())
    }
    J002.findVerb(j002Array, 19690720).also {
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

}