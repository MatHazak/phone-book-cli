package phonebook

import java.io.File
import java.util.Scanner

var linearSortDuration = 0L

fun main() {
    val dictionaryFile = File("./resources/dictionary.txt")
    val targetsFile = File("./resources/targets.txt")
    val dictionary = getDictionary(dictionaryFile)
    val targets = getTargets(targetsFile)
    println("Start searching (linear search)...")
    findByLnSearch(dictionary, targets)

    println("\nStart searching (bubble sort + jump search)...")
    findByBubbleSortAndJumpSearch(dictionary.toMutableList(), targets)

    println("\nStart searching (quick sort + binary search)...")
    findByQuickSortAndBinarySearch(dictionary.toMutableList(), targets)

    println("\nStart searching (hash table)...")
    findByHashTable(dictionary.toMutableList(), targets)
}

fun getDictionary(file: File): MutableList<Person> {
    val list = mutableListOf<Person>()
    val dictionaryScanner = Scanner(file)
    while(dictionaryScanner.hasNext()) {
        val phoneNumber = dictionaryScanner.next()
        val name = dictionaryScanner.nextLine().trim()
        list += Person(name, phoneNumber)
    }
    return list
}

fun getTargets(file: File): List<Person> {
    val list = mutableListOf<Person>()
    val targetScanner = Scanner(file)
    while (targetScanner.hasNext())
        list.add(Person(targetScanner.nextLine()))
    return list
}

fun findByLnSearch(dictionary: MutableList<Person>, targets: List<Person>) {
    val init = System.currentTimeMillis()
    val counts = countFoundedTargets(targets, dictionary, Search.Companion::linear)
    val end = System.currentTimeMillis()
    linearSortDuration = end - init
    val duration = getTimeInfo(linearSortDuration)
    println("Found $counts / ${targets.size} entries. Time taken: $duration")
}

fun findByBubbleSortAndJumpSearch(dictionary: MutableList<Person>, targets: List<Person>) {
    var searchMethod: (Person, List<Person>) -> Boolean
    var sortedDictionary: MutableList<Person>
    var sorted: Boolean
    val initSort = System.currentTimeMillis()
    try {
        sortedDictionary = Sort.bubble(dictionary, linearSortDuration * 10)
        sorted = true
        searchMethod = Search.Companion::jump

    } catch (e: RuntimeException) {
        sorted = false
        sortedDictionary = dictionary
        searchMethod = Search.Companion::linear
    }
    val endSort = System.currentTimeMillis()
    val sortDuration = getTimeInfo(endSort - initSort)
    val initSearch = System.currentTimeMillis()
    val counts = countFoundedTargets(targets, sortedDictionary, searchMethod)
    val endSearch = System.currentTimeMillis()
    val searchDuration = getTimeInfo(endSearch - initSearch)
    val totalDuration = getTimeInfo(endSearch - initSort)
    println("Found $counts / ${targets.size} entries. Time taken: $totalDuration")
    println("Sorting time: $sortDuration${if (sorted) "" else " - STOPPED, moved to linear search"}")
    println("Searching time: $searchDuration")
}

fun findByQuickSortAndBinarySearch(dictionary: MutableList<Person>, targets: List<Person>) {
    val initSort = System.currentTimeMillis()
    val sortedDictionary = Sort.quick(dictionary)
    val endSort = System.currentTimeMillis()
    val sortDuration = getTimeInfo(endSort - initSort)
    val initSearch = System.currentTimeMillis()
    val counts = countFoundedTargets(targets, sortedDictionary, Search.Companion::binary)
    val endSearch = System.currentTimeMillis()
    val searchDuration = getTimeInfo(endSearch - initSearch)
    val totalDuration = getTimeInfo(endSearch - initSort)
    println("Found $counts / ${targets.size} entries. Time taken: $totalDuration")
    println("Sorting time: $sortDuration")
    println("Searching time: $searchDuration")
}

fun findByHashTable(dictionary: MutableList<Person>, targets: List<Person>) {
    val initCreate = System.currentTimeMillis()
    val dictionaryMap = HashTable<String, String>(510)
    dictionary.forEach { dictionaryMap.put(it.name, it.phoneNumber)}
    val endCreate = System.currentTimeMillis()
    val createDuration = getTimeInfo(endCreate - initCreate)
    val initSearch = System.currentTimeMillis()
    var counts = 0
    targets.forEach { if (dictionaryMap.get(it.name) != null) counts++}
    val endSearch = System.currentTimeMillis()
    val searchDuration = getTimeInfo(endSearch - initSearch)
    val totalDuration = getTimeInfo(endSearch - initCreate)
    println("Found $counts / ${targets.size} entries. Time taken: $totalDuration")
    println("Creating time: $createDuration")
    println("Searching time: $searchDuration")
}

fun<T> countFoundedTargets(targets: List<T>, dictionary: List<T>, search: (T, List<T>) -> Boolean): Int {
    var counter = 0
    targets.forEach {
        if (search(it, dictionary)) counter++
    }
    return counter
}

fun getTimeInfo(duration: Long): String {
    val min = duration / (60 * 1000)
    val sec = duration % (60 * 1000) / 1000
    val ms = duration % 1000
    return "$min min. $sec sec. $ms ms."
}

class Person(val name: String, val phoneNumber: String = "-1"): Comparable<Person> {
    override fun toString(): String {
        return "$name $phoneNumber"
    }

    override fun compareTo(other: Person): Int {
        return this.name.compareTo(other.name)
    }

    override fun equals(other: Any?): Boolean {
        if (other is Person) return this.name == other.name
        return super.equals(other)
    }

    override fun hashCode() = 11 * name.hashCode() + phoneNumber.hashCode()
}