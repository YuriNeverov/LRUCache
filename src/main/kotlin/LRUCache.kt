class LRUCache<K, V>(private val maxCacheSize: Int) {
    data class Node<K, V>(val key: K, var value: V, var prev: Node<K, V>? = null, var next: Node<K, V>? = null)

    init {
        if (maxCacheSize < 1) {
            throw IllegalArgumentException()
        }
    }

    private var first: Node<K, V>? = null
    private var last: Node<K, V>? = null
    var size: Int = 0
        private set

    private val map: MutableMap<K, Node<K, V>> = HashMap()

    fun containsKey(key: K) : Boolean {
        return map.containsKey(key)
    }

    operator fun get(key: K) : V {
        if (!map.containsKey(key)) {
            throw NoSuchElementException()
        }
        lift(key)
        return map[key]!!.value
    }

    operator fun set(key: K, value: V) {
        val oldSize = size
        if (containsKey(key)) {
            map[key]!!.value = value
            lift(key)
            return
        }
        if (size >= maxCacheSize) {
            evictLRU()
        }
        val new = Node(key, value, first)
        map[key] = new

        first?.next = new
        first = new
        if (last == null) {
            last = new
        }
        size++

        assert(last != null)
        assert(first != null)
        assert(size in 1..maxCacheSize)
        assert(size == maxCacheSize || size == oldSize + 1)
    }

    private fun lift(key: K) {
        val oldSize = size
        assert(containsKey(key))
        val liftee = map[key]!!
        if (liftee == first) {
            return
        }
        assert(size > 1)
        assert(first != null)
        if (last == liftee) {
            last = liftee.next
        }

        liftee.prev?.next = liftee.next
        liftee.next?.prev = liftee.prev

        liftee.next = null
        liftee.prev = first
        first!!.next = liftee
        first = liftee

        assert(last!!.prev == null)
        assert(first!!.next == null)
        assert(first == liftee)
        assert(size == oldSize)
    }

    private fun evictLRU() {
        val oldSize = size
        assert(last != null && first != null)
        map.remove(last!!.key)

        if (last == first) {
            assert(size == 1)
            size = 0
            last = null
            first = null
            return
        }
        last!!.next!!.prev = null
        last = last!!.next!!
        size--

        assert(last!!.prev == null)
        assert(size == 0 || size == oldSize - 1)
    }
}