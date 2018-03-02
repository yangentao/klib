package yet.util

import java.util.*

/**
 * Created by yangentao on 16/3/12.
 */

class MultiMap<K, V> {
    val map = HashMap<K, LinkedList<V>>()

    operator fun set(key: K, value: V) {
        var ls = map.get(key) //map[key]
        if (ls != null) {
            if (!ls.contains(value)) {
                ls.add(value)
            }
        } else {
            ls = LinkedList<V>()
            ls.add(value)
            map[key] = ls
        }
    }

    operator fun get(key: K): LinkedList<V>? {
        return map[key]
    }

    fun contains(key: K): Boolean {
        return key in map
    }

    fun remove(key: K) {
        map.remove(key)
    }

    fun removeValue(key: K, value: V) {
        val ls = get(key)
        ls?.remove(value)
    }

    fun removeValue(value: V) {
        for (ls in map.values) {
            ls.remove(value)
        }
    }


}