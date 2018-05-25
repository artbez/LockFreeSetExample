package iimetra.example.concurrent.set

import org.apache.logging.log4j.LogManager
import org.junit.Test

class OneThreadTest {

    @Test
    fun containsTest() {
        val mySet = LockFreeSetImpl<Int>()
        mySet.add(7)
        mySet.add(3)
        mySet.add(5)

        assert(mySet.contains(3))
        assert(mySet.contains(5))
        assert(mySet.contains(7))
        assert(!mySet.contains(6))
    }

    @Test
    fun uniqueTest() {
        val mySet = LockFreeSetImpl<Int>()
        mySet.add(7)
        mySet.add(7)
        mySet.add(5)
        mySet.add(7)

        assert(mySet.remove(7))
        assert(!mySet.contains(7))
        assert(mySet.contains(5))
    }

    @Test
    fun removeTest() {
        val mySet = LockFreeSetImpl<Int>()
        mySet.add(7)
        mySet.add(3)
        mySet.add(5)

        mySet.remove(5)
        assert(!mySet.contains(5))
        assert(!mySet.isEmpty())

        mySet.remove(3)
        assert(!mySet.contains(3))
        assert(!mySet.isEmpty())

        mySet.remove(7)
        assert(!mySet.contains(7))
        assert(mySet.isEmpty())
    }
}