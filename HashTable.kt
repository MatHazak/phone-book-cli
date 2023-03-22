package phonebook

import kotlin.math.abs

class HashTable<K, V> (private var bucketsNumber: Int = 16, private val maxLoadFactor: Double = 0.75) {
    private var size: Int = 0
    private var table = arrayOfNulls<Node>(bucketsNumber)

    fun put(key: K, value: V) {
        val index = getBucketIndex(key)
        val node = Node(key, value)
        if (table[index] == null)
            table[index] = node
        else {
            node.next = table[index]
            table[index] = node
        }
        size++
        checkLoadFactor()
    }

    fun get(key: K): V? {
        val index = getBucketIndex(key)
        var node = table[index]
        while (node != null) {
            if (node.key == key) return node.value
            node = node.next
        }
        return null
    }

    fun remove(key: K): Boolean {
        val index = getBucketIndex(key)
        var node = table[index]
        if (node != null) {
            if (node.key == key) {
                table[index] = node.next
                return true
            }
            while (node!!.next != null) {
                if (node.next!!.key == key) {
                    node.next = node.next!!.next
                    return true
                }
                node = node.next
            }
        }
        return false
    }

    fun size(): Int {
        return this.size
    }

    private fun getBucketIndex(key: K): Int {
        val hash = key.hashCode()
        return abs(hash % bucketsNumber)
    }

    private fun checkLoadFactor() {
        val loadFactor = size.toDouble() / bucketsNumber
        if (loadFactor > maxLoadFactor) {
            replenishTable()
        }
    }

    private fun replenishTable() {
        bucketsNumber *= 2
        val tempTable = table
        table = arrayOfNulls(bucketsNumber)
        for (node in tempTable) {
            if (node == null) continue
            put(node.key, node.value)
            var nextNode = node.next
            while (nextNode != null) {
                put(nextNode.key, nextNode.value)
                nextNode = nextNode.next
            }
        }
    }

    inner class Node(val key: K, val value: V, var next: Node? = null)
}

