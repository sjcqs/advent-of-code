import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun interface InputMapper<T> {
    fun map(input: String): T
}

abstract class Puzzle<InputType, OutputType>(private val mapper: InputMapper<InputType>) {
    protected abstract val inputFileName: String
    protected var debugging: Boolean = false

    fun runBlocking(fileName: String = inputFileName, debugging: Boolean = false): OutputType {
        return runBlocking { run(fileName, debugging) }
    }

    protected fun log(message: String) {
        if (debugging) println("${this.javaClass.simpleName}: $message")
    }

    protected fun log(throwable: Throwable, message: String? = null) {
        if (debugging) {
            print("${this.javaClass.simpleName}: $message")
            throwable.printStackTrace()
        }
    }

    suspend fun run(fileName: String = inputFileName, debugging: Boolean = false): OutputType {
        this.debugging = debugging
        var output: OutputType
        println("${this::class.java.simpleName}: Started")
        val duration = measureTimeMillis {
            val input = mapper.map(loadResource(fileName).trim())
            output = doJob(input)
        }
        println("${this::class.java.simpleName}: Finished -> $output ($duration ms)")
        return output
    }

    private inline fun <T> measureTimeMillis(
        loggingFunction: (Long) -> Unit,
        function: () -> T
    ): T {
        val startTime = System.currentTimeMillis()
        val result: T = function.invoke()
        loggingFunction.invoke(System.currentTimeMillis() - startTime)

        return result
    }

    protected abstract suspend fun doJob(input: InputType): OutputType
}