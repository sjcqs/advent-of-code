package puzzle10

import InputMapper
import Puzzle

object Puzzle10 : Puzzle<List<Long>, Puzzle10.Output>(InputMapper { input ->
    input.split("\n").map(String::toLong)
}) {
    override val inputFileName: String = "10.txt"

    override suspend fun doJob(input: List<Long>): Output {
        val devicesChain = adaptersChain(input)
        val differencesProduct = computeDifferencesProduct(devicesChain)
        val combination = computeCombination(devicesChain)
        return Output(differencesProduct, combination)
    }

    private fun computeCombination(devicesChain: List<Long>): Long {
        val counts = mutableMapOf<Int, Long>()
        devicesChain.foldRightIndexed(counts) { index, joltage, _ ->
            val maxJoltage = joltage + 3

            var count = counts.getOrDefault(index + 1, 1L)
            if (index + 2 in devicesChain.indices && devicesChain[index + 2] <= maxJoltage) {
                count += counts.getOrDefault(index + 2, 0L)
            }
            if (index + 3 in devicesChain.indices && devicesChain[index + 3] <= maxJoltage) {
                count += counts.getOrDefault(index + 3, 0L)
            }
            counts[index] = count
            counts

        }
        return counts.getOrDefault(0, -1L)
    }

    private fun computeDifferencesProduct(devicesChain: List<Long>): Int {
        val differencesOf1 = devicesChain.filterIndexed { index, joltage ->
            val previousJoltage = devicesChain.getOrNull(index - 1)
            previousJoltage != null && previousJoltage == joltage - 1
        }.size

        val differencesOf3 = devicesChain.filterIndexed { index, joltage ->
            val previousJoltage = devicesChain.getOrNull(index - 1)
            previousJoltage != null && previousJoltage == joltage - 3
        }.size

        return differencesOf1 * differencesOf3
    }

    private fun adaptersChain(input: List<Long>): List<Long> {
        val deviceAdapterJoltage = requireNotNull(input.maxOrNull()) + 3
        val chain = input.toMutableList()
        chain.sort()
        chain.add(0, 0)
        chain.add(deviceAdapterJoltage)
        return chain
    }

    data class Output(
        val differencesProduct: Int,
        val combinationCount: Long
    )
}