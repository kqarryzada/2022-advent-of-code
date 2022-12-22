package problem03

import java.io.File
import kotlin.RuntimeException

private const val INPUT_FILE = "input.txt"

/**
 * Obtains the "priority" of the given character. `a` through `z` refers to a
 * priority of 1 - 26, and `A` through `Z` refers to 27 - 52.
 */
private fun getPriority(char: Char): Int {
    val lowerChar = char.lowercaseChar()
    if (lowerChar < 'a' || lowerChar > 'z') { throw IllegalArgumentException("Invalid character '$char' requested.") }

    var priority = lowerChar - 'a' + 1
    if (char.isUpperCase()) { priority += 26 }

    return priority
}

fun main() {
    var prioritySum = 0L
    val reader = File(INPUT_FILE).bufferedReader()
    while (true) {
        val sack1 = reader.readLine()
        val sack2 = reader.readLine()
        val sack3 = reader.readLine()
        if (sack1.isNullOrEmpty()) { break }
        if (sack2.isNullOrEmpty() || sack3.isNullOrEmpty()) { throw RuntimeException("Invalid file input.") }

        // Assemble a set of candidates based on the intersection of the first
        // two rucksacks.
        val candidateSet = mutableSetOf<Char>()
        sack1.forEach {
            if (sack2.contains(it)) { candidateSet.add(it) }
        }

        // Iterate over the entire third rucksack and ensure there is only one
        // common element.
        var found = false
        var letter = '0'
        sack3.forEach {
            if (candidateSet.contains(it)) {
                // We should not have found another letter that exists in all
                // three rucksacks.
                if (found && it != letter) {
                    throw RuntimeException("Found an additional letter, '$it', in the candidate set.")
                }
                found = true
                letter = it
            }
        }
        if (!found) { throw RuntimeException("Unable to find an element that appears in all three rucksacks.") }

        prioritySum += getPriority(letter)
    }
    reader.close()

    println("The sum of the priorities is: $prioritySum")
}
