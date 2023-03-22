package phonebook

class Sort {
    companion object {
        fun<T: Comparable<T>> bubble(list: MutableList<T>, timeLimit: Long): MutableList<T> {
            val initSort = System.currentTimeMillis()
            repeat(list.size) {
                var temp: T
                for (i in 0 until list.size - 1) {
                    if (list[i] > list[i + 1]) {
                        temp = list[i]
                        list[i] = list[i + 1]
                        list[i + 1] = temp
                    }
                }
                val duration = System.currentTimeMillis() - initSort
                if (duration > timeLimit) throw RuntimeException("time limit exceeded.")
            }
            return list.toMutableList()
        }

        fun<T: Comparable<T>> quick(list: MutableList<T>): MutableList<T> {
            if (list.size < 2) return list
            val pivot = list.last()
            val prePivot = mutableListOf<T>()
            val repeatedPivot = mutableListOf<T>()
            val postPivot = mutableListOf<T>()
            list.forEach {
                if (it < pivot) prePivot.add(it)
                else if (it == pivot) repeatedPivot.add(it)
                else postPivot.add(it)
            }
            return (quick(prePivot) + repeatedPivot + quick(postPivot)).toMutableList()
        }

        fun<T: Comparable<T>> merge(list: MutableList<T>): MutableList<T> {
            if (list.size == 1) return list
            val preList = merge(list.subList(0, list.size / 2).toMutableList())
            val postList = merge(list.subList(list.size / 2, list.size).toMutableList())
            val sorted = mutableListOf<T>()
            var i = 0
            var j = 0
            while (i < preList.size || j < postList.size) {
                when {
                    i < preList.size && j < postList.size -> {
                        if (preList[i] <= postList[j]) sorted.add(preList[i++])
                        else sorted.add(postList[j++])
                    }
                    i < preList.size -> sorted.add(preList[i++])
                    else -> sorted.add(postList[j++])
                }
            }
            return sorted
        }
    }
}