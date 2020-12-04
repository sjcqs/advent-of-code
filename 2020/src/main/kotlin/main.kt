import kotlinx.coroutines.runBlocking
import puzzle01.Puzzle01Part01
import puzzle01.Puzzle01Part02
import puzzle02.Puzzle02Part01
import puzzle02.Puzzle02Part02
import puzzle03.Puzzle03Part01
import puzzle03.Puzzle03Part02
import puzzle04.Puzzle04Part01
import puzzle04.Puzzle04Part02

fun main() {
    runBlocking {
        Puzzle01Part01.run()
        Puzzle01Part02.run()
        Puzzle02Part01.run()
        Puzzle02Part02.run()
        Puzzle03Part01.run()
        Puzzle03Part02.run()
        Puzzle04Part01.run()
        Puzzle04Part02.run()
    }
}