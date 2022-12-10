package puzzle10

import readInput
import requireEquals
import split

private const val INITIAL_X = 1

private sealed interface Command {
    val duration: Int

    object NoOp : Command {
        override val duration: Int
            get() = 1
    }

    data class AddX(val value: Int) : Command {
        override val duration: Int
            get() = 2
    }

    companion object {
        fun parse(line: String): Command {
            val parts = line.split(" ")
            val command = parts.first()
            val arg = parts.lastOrNull()

            return if (command == "addx") {
                AddX(arg!!.toInt())
            } else {
                NoOp
            }
        }
    }
}

private fun String.parse(): List<Command> {
    return trim().split()
        .map(Command::parse)
}

private fun Map<Int, Int>.registryAt(cycle: Int): Int {
    return getOrElse(cycle) {
        if (isEmpty()) return INITIAL_X
        val firstCycle = keys.first()
        if (cycle < firstCycle) {
            return INITIAL_X
        }
        val lastCycle = keys.last()
        if (cycle > lastCycle) {
            return getOrDefault(lastCycle, INITIAL_X)
        }
        val (cycleBefore, _) = entries.windowed(2).firstOrNull { (cycleBefore, cycleAfter) ->
            cycle in cycleBefore.key until cycleAfter.key
        } ?: return@getOrElse INITIAL_X

        return cycleBefore.value
    }
}

private fun List<Command>.simulate(x: Int = INITIAL_X): Map<Int, Int> {
    return buildMap {
        var cycle = 0
        var x = x
        this@simulate.forEach { command ->
            cycle += command.duration
            if (command is Command.AddX) {
                x += command.value
            }
            put(cycle, x)
        }
    }
}

private fun Map<Int, Int>.signals(): List<Int> {
    var cycle = 19
    val lastCycle = keys.lastOrNull() ?: 0
    return buildList {
        while (cycle < lastCycle) {
            val x = registryAt(cycle)
            add(x * (cycle + 1))
            cycle += 40
        }
    }
}

fun main() {
    val simple = readInput("example/10bis.txt").parse()
    val example = readInput("example/10.txt").parse()
    val input = readInput("10.txt").parse()

    requireEquals(mapOf(1 to 1, 3 to 4, 5 to -1), simple.simulate())
    val exampleRegistryMap = example.simulate()
    val inputRegistryMap = input.simulate()

    requireEquals(13140, exampleRegistryMap.signals().sum())
    println(inputRegistryMap.signals().sum())

    /*requireEquals(
        """
        |##..##..##..##..##..##..##..##..##..##..
        |###...###...###...###...###...###...###.
        |####....####....####....####....####....
        |#####.....#####.....#####.....#####.....
        |######......######......######......####
        |#######.......#######.......#######.....
        |
    """.trimMargin(), exampleRegistryMap.draw()
    )*/
    println(inputRegistryMap.draw())
}

private fun Map<Int, Int>.draw(): String {
    return buildString {
        repeat(240) { position ->
            val registry = registryAt(position) - 1
            if ((position % 40) in registry..registry + 2) {
                append("#")
            } else {
                append(".")
            }
            if ((position + 1) % 40 == 0) appendLine()
        }
    }
}

