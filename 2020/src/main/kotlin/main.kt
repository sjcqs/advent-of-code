import kotlinx.coroutines.runBlocking
import puzzle01.Puzzle01Part01
import puzzle01.Puzzle01Part02
import puzzle02.Puzzle02Part01
import puzzle02.Puzzle02Part02
import puzzle03.Puzzle03Part01
import puzzle03.Puzzle03Part02
import puzzle04.Puzzle04Part01
import puzzle04.Puzzle04Part02
import puzzle05.Puzzle05Part01
import puzzle05.Puzzle05Part02
import puzzle06.Puzzle06Part01
import puzzle06.Puzzle06Part02
import puzzle07.Puzzle07Part01
import puzzle07.Puzzle07Part02
import puzzle08.Puzzle08Part01
import puzzle08.Puzzle08Part02
import puzzle09.Puzzle09
import puzzle10.Puzzle10
import puzzle11.Puzzle11

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
        Puzzle05Part01.run()
        Puzzle05Part02.run()
        Puzzle06Part01.run()
        Puzzle06Part02.run()
        Puzzle07Part01.run()
        Puzzle07Part02.run()
        Puzzle08Part01.run()
        Puzzle08Part02.run()
        Puzzle09(25).run()
        Puzzle10.run()
        Puzzle11.run()
    }
}