import java.io.File
import java.util.Hashtable

fun linearSearch(directoryLines: List<String>, whatWeAreLookingFor: List<String>): Int {
    var numberOfFoundedEntries = 0

    for (w in whatWeAreLookingFor) {
        for (d in directoryLines) {
            if (d.contains(w)) {
                numberOfFoundedEntries++
                break
            }
        }
    }

    return numberOfFoundedEntries
}

fun bubbleSort(directoryLines: List<String>, linearSearchTime: Long): List<String>? {
    val start = System.currentTimeMillis()
    val sortedList = directoryLines.toMutableList()
    val n = directoryLines.size
    var temp: String
    for (i in 0 until n - 1) {
        for (j in 0 until n - i - 1) {
            val timePoint = System.currentTimeMillis()
//            println("NOW: ${timeCount(timePoint-start)} linear search ${timeCount(linearSearchTime*10)}")
            if((timePoint-start) > linearSearchTime * 10){
                return null
            }
            if (sortedList[i].compareTo(sortedList[j]) > 0) {
                temp = sortedList[i]
                sortedList[i] = sortedList[j]
                sortedList[j] = temp
            }
        }
    }
    return sortedList
}

fun jumpSearch(directoryLines: List<String>, whatWeAreLookingFor: String): Int {
    val n = directoryLines.size
    val step = Math.sqrt(n.toDouble()).toInt()
    var prev = 0

    while (directoryLines[Math.min(step, n) - 1] < whatWeAreLookingFor) {
        prev = step
        if (prev >= n) return -1
    }

    for (i in prev until Math.min(step, n)) {
        if (directoryLines[i] == whatWeAreLookingFor) return i
    }

    return -1
}

fun jumpSearch(directoryLines: List<String>, whatWeAreLookingFor: List<String>): Int {
    var numberOfFoundedEntries = 0

    for (entry in whatWeAreLookingFor) {
        if (jumpSearch(directoryLines, entry) != -1) {
            numberOfFoundedEntries++
        }
    }

    return numberOfFoundedEntries
}

fun quickSort(directoryLines: List<String>): List<String> {
    if (directoryLines.size < 2) return directoryLines
    val pivot = directoryLines[directoryLines.size / 2]
    val equal = directoryLines.filter { it == pivot }
    val less = directoryLines.filter { it < pivot }
    val greater = directoryLines.filter { it > pivot }
    return quickSort(less) + equal + quickSort(greater)
}

fun binarySearch(directoryLines: List<String>, target: String): Int {
    var left = 0
    var right = directoryLines.size - 1

    while (left <= right) {
        val mid = (left + right) / 2
        when {
            directoryLines[mid] == target -> return mid
            directoryLines[mid] < target -> left = mid + 1
            else -> right = mid - 1
        }
    }
    return -1
}

fun binarySearch(directoryLines: List<String>, whatWeAreLookingFor: List<String>): Int {
    var foundedCounter = 0
    for (entry in whatWeAreLookingFor) {
        if (binarySearch(directoryLines, entry) == -1) {
            foundedCounter++
        }
    }
    return foundedCounter
}


fun timeCount(start: Long, end: Long): String {
    val difference = (end - start)
    val minutes = difference / 60000
    val seconds = (difference % 60000) / 1000
    val remainingMilliseconds = difference % 1000
    return "${minutes} min. ${seconds} sec. ${remainingMilliseconds} ms."
}

fun timeCount(timeDifference: Long): String {
    val minutes = timeDifference / 60000
    val seconds = (timeDifference % 60000) / 1000
    val remainingMilliseconds = timeDifference % 1000
    return "${minutes} min. ${seconds} sec. ${remainingMilliseconds} ms."
}

fun createHashTable(directoryLines: List<String>): Hashtable<String, Int> {
    val hashTable = Hashtable<String, Int>()
    for (entry in directoryLines) {
        val number = entry.substringBefore(' ')
        val name = entry.substringAfter(' ')
        hashTable[name] = number.toInt()
    }
    return hashTable
}

fun findInHashTable(hashtable: Hashtable<String, Int>, whatWeAreLookingFor: List<String>): Int{
    var foundedCounter = 0
    for(entry in whatWeAreLookingFor){
        if(hashtable.get(entry) != null){
            foundedCounter++
        }
    }
    return foundedCounter
}

fun main() {
    val separator = File.separator
    val directoryLines =
        File("C:${separator}Users${separator}Мирослав Левдиков${separator}directory.txt").readLines()
    val findLines = File("C:${separator}Users${separator}Мирослав Левдиков${separator}find.txt").readLines()

    println("Start searching (linear search)...")
    val linearStart = System.currentTimeMillis()
    val linearResult = linearSearch(directoryLines, findLines)
    val linearStop = System.currentTimeMillis()
    println("Found $linearResult / ${findLines.size} entries. ${timeCount(linearStart, linearStop)}")

    println("\nStart searching (bubble sort + jump search)...")
    val bubbleSortStart = System.currentTimeMillis()
    val bubbleSortResult = bubbleSort(directoryLines, (linearStop-linearStart))
    val bubbleSortStop = System.currentTimeMillis()
    val bubbleSortTime = bubbleSortStop - bubbleSortStart

    if(bubbleSortResult != null) {
        val jumpSearchStart = System.currentTimeMillis()
        val jumpSearchResult = jumpSearch(bubbleSortResult, findLines)
        val jumpSearchStop = System.currentTimeMillis()

        val jumpSearchTime = jumpSearchStop - jumpSearchStart
        println("Found $jumpSearchResult / ${findLines.size} entries. ${timeCount((bubbleSortTime + jumpSearchTime))}")
        println("Sorting time: ${timeCount(bubbleSortTime)}")
        println("Searching time: ${timeCount(jumpSearchTime)}")
    } else {
        val linearStart = System.currentTimeMillis()
        val linearResult = linearSearch(directoryLines, findLines)
        val linearStop = System.currentTimeMillis()

        val linearTime = linearStop - linearStart
        println("Found $linearResult / ${findLines.size} entries. ${timeCount((bubbleSortTime+linearTime))}")
        println("Sorting time: ${timeCount(bubbleSortTime)} - STOPPED, moved to linear search")
        println("Searching time: ${timeCount(linearTime)}")
    }


    println("\nStart searching (quick sort + binary search)...")
    val quickSortStart = System.currentTimeMillis()
    val quickSortResult = quickSort(directoryLines)
    val quickSortStop = System.currentTimeMillis()

    val binarySearchStart = System.currentTimeMillis()
    val binarySearchResult = binarySearch(quickSortResult, findLines)
    val binarySearchStop = System.currentTimeMillis()

    val quickSortTime = quickSortStop - quickSortStart
    val binarySearchTime = binarySearchStop - binarySearchStart
    println("Found $binarySearchResult / ${findLines.size} entries. ${timeCount((quickSortTime + binarySearchTime))}")
    println("Sorting time: ${timeCount(quickSortTime)}")
    println("Searching time: ${timeCount(binarySearchTime)}")

    println("\nStart searching (hash table)...")
    val hashTableCreationStart = System.currentTimeMillis()
    val hashtable = createHashTable(directoryLines)
    val hashTableCreationStop = System.currentTimeMillis()

    val searchInHashTableStart = System.currentTimeMillis()
    val searchInHashTableStartResult = findInHashTable(hashtable, findLines)
    val searchInHashTableStop = System.currentTimeMillis()

    val hashTableCreationTime = hashTableCreationStop - hashTableCreationStart
    val searchInHashTableTime = searchInHashTableStop - searchInHashTableStart
    println("Found $searchInHashTableStartResult / ${findLines.size} entries. ${timeCount((searchInHashTableTime + hashTableCreationTime))}")
    println("Creating time: ${timeCount(hashTableCreationTime)}")
    println("Searching time: ${timeCount(searchInHashTableTime)}")
}