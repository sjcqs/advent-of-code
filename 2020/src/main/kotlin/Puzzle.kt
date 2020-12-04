import kotlinx.coroutines.runBlocking

interface InputMapper<T> {
    fun map(input: String): T
}

abstract class Puzzle<InputType, OutputType>(private val mapper: InputMapper<InputType>) {
    protected abstract val inputFileName: String

    fun runBlocking(fileName: String = inputFileName): OutputType = runBlocking { run(fileName) }

    suspend fun run(fileName: String = inputFileName): OutputType {
        val input = mapper.map(loadResource(fileName))
        println("${this::class.java.simpleName}: Started")
        val output = doJob(input)
        println("${this::class.java.simpleName}: Finished -> $output")
        return output
    }

    protected abstract suspend fun doJob(input: InputType): OutputType
}