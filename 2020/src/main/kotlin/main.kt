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
import puzzle12.Puzzle12
import puzzle13.Puzzle13
import puzzle14.Puzzle14
import puzzle15.Puzzle15
import puzzle16.Puzzle16
import puzzle17.Puzzle17
import puzzle18.Puzzle18

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
        Puzzle12.run()
        Puzzle13.run()
        Puzzle14.run()
        Puzzle15(2020).run()
        //Puzzle15(30_000_000).run()
        Puzzle16.run()
        Puzzle17.run()
        Puzzle18.run()
    }
}