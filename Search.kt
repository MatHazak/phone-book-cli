package phonebook

import kotlin.math.min
import kotlin.math.sqrt

class Search {
    companion object {
        fun<T: Comparable<T>> linear(target: T, list: List<T>): Boolean {
            list.forEach {
                if (it == target) return true
            }
            return false
        }

        fun<T: Comparable<T>> jump(target: T, list: List<T>): Boolean {
            val step = sqrt(list.size.toDouble()).toInt()
            var current = 0
            var next = step
            while (list[next] < target) {
                current += step
                if (current > list.lastIndex) return false
                next = min(next + step, list.lastIndex)
            }
            for (i in current..next) {
                if (list[i] == target) return true
            }
            return false
        }

        fun<T: Comparable<T>> binary(target: T, list: List<T>): Boolean {
            var left = 0
            var right = list.lastIndex
            while (left <= right) {
                val mid = (left + right) / 2
                when {
                    list[mid] == target -> return true
                    list[mid] > target -> right = mid - 1
                    else -> left = mid + 1
                }
            }
            return false
        }
    }
}
