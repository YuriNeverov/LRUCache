import kotlin.test.*

internal class LRUCacheTest {
    @Test
    fun getSizeAndSet() {
        val lruCache = LRUCache<Int, Int>(2)
        assertEquals(0, lruCache.size)

        lruCache[0] = 1
        assertEquals(1, lruCache.size)

        lruCache[0] = 2
        assertEquals(1, lruCache.size)

        lruCache[1] = 3
        assertEquals(2, lruCache.size)

        lruCache[1] = 4
        assertEquals(2, lruCache.size)

        lruCache[2] = 5
        assertEquals(2, lruCache.size)
    }

    @Test
    fun setAndContainsKey() {
        val lruCache = LRUCache<Int, Int>(2)
        assertFalse(lruCache.containsKey(0))
        assertFalse(lruCache.containsKey(1))
        assertFalse(lruCache.containsKey(2))

        lruCache[0] = 1
        assertTrue(lruCache.containsKey(0))
        assertFalse(lruCache.containsKey(1))
        assertFalse(lruCache.containsKey(2))

        lruCache[1] = 2
        assertTrue(lruCache.containsKey(0))
        assertTrue(lruCache.containsKey(1))
        assertFalse(lruCache.containsKey(2))

        lruCache[2] = 3
        assertFalse(lruCache.containsKey(0))
        assertTrue(lruCache.containsKey(1))
        assertTrue(lruCache.containsKey(2))

        lruCache[3] = 4
        assertFalse(lruCache.containsKey(0))
        assertFalse(lruCache.containsKey(1))
        assertTrue(lruCache.containsKey(2))
        assertTrue(lruCache.containsKey(3))
    }

    @Test
    fun setAndGet() {
        val lruCache = LRUCache<Int, Int>(2)
        assertFailsWith(NoSuchElementException::class) {
            lruCache[0]
        }
        lruCache[0] = 1
        assertEquals(1, lruCache[0])

        lruCache[1] = 2
        assertEquals(1, lruCache[0])
        assertEquals(2, lruCache[1])

        lruCache[0] = 3
        assertEquals(3, lruCache[0])
        assertEquals(2, lruCache[1])

        lruCache[2] = 4
        assertFailsWith(NoSuchElementException::class) {
            lruCache[0]
        }
        assertEquals(2, lruCache[1])
        assertEquals(4, lruCache[2])
    }

    @Test
    fun fourElementsLRUTest() {
        val lruCache = LRUCache<Int, Int>(4)
        assertEquals(0, lruCache.size)
        assertFailsWith(NoSuchElementException::class) {
            lruCache[1337]
        }
        assertFailsWith(NoSuchElementException::class) {
            lruCache[0]
        }
        assertFalse(lruCache.containsKey(0))

        lruCache[0] = 10
        lruCache[10] = 20
        assertEquals(2, lruCache.size)
        assertEquals(10, lruCache[0])
        assertEquals(20, lruCache[10])

        lruCache[20] = 30
        assertEquals(3, lruCache.size)
        assertEquals(10, lruCache[0])
        assertEquals(20, lruCache[10])
        assertEquals(30, lruCache[20])

        lruCache[30] = 40
        assertEquals(4, lruCache.size)
        assertEquals(10, lruCache[0])
        assertEquals(20, lruCache[10])
        assertEquals(30, lruCache[20])
        assertEquals(40, lruCache[30])

        lruCache[40] = 50
        assertEquals(4, lruCache.size)
        assertFailsWith(NoSuchElementException::class) {
            lruCache[0]
        }
        assertEquals(20, lruCache[10])
        assertEquals(30, lruCache[20])
        assertEquals(40, lruCache[30])
        assertEquals(50, lruCache[40])

        lruCache[10]
        lruCache[30]
        lruCache[0] = 0
        assertFalse(lruCache.containsKey(20))

        lruCache[10]
        lruCache[40] = 1
        lruCache[1] = 2
        assertFalse(lruCache.containsKey(30))
    }
}