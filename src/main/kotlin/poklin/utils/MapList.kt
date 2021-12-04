package poklin.utils

class MapList<K, V> : Iterable<List<V>?> {
    private val mapList: MutableMap<K, MutableList<V>> = HashMap()
    override fun iterator(): MutableIterator<List<V>> {
        return mapList.values.iterator()
    }

    fun add(key: K, value: V): V {
        var values = mapList[key]
        if (values == null) {
            values = ArrayList()
            mapList[key] = values
        }
        values.add(value)
        return value
    }

    fun keySet(): Set<K> {
        return mapList.keys
    }

    fun entrySet(): Set<Map.Entry<K, List<V>>> {
        return mapList.entries
    }

    operator fun get(key: K): List<V> {
        return mapList[key]!!
    }
}