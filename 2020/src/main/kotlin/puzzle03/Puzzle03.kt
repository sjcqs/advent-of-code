package puzzle03

import Puzzle

abstract class Puzzle03(slopes: List<Slope>) : Puzzle<SlopeConfiguration, Long>(Puzzle03Mapper(slopes)) {

    final override val inputFileName: String
        get() = "Puzzle03.txt"

    final override suspend fun doJob(input: SlopeConfiguration): Long {
        return input.encounteredTreeProduct()
    }

}