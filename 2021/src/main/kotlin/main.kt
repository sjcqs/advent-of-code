import kotlinx.coroutines.runBlocking
import puzzle01.Puzzle01Part01
import puzzle01.Puzzle01Part02
import puzzle02.Puzzle02Part01
import puzzle02.Puzzle02Part02


fun main() {
    runBlocking {
        Puzzle01Part01.run()
        Puzzle01Part02.run()
        Puzzle02Part01.run("example/02.txt")
        Puzzle02Part01.run()
        Puzzle02Part02.run("example/02.txt")
        Puzzle02Part02.run()
    }
}