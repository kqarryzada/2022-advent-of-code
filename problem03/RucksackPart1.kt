package problem03

import java.io.File
import java.lang.RuntimeException

private const val INPUT_FILE = "input.txt"

private fun getPriority(char: Char): Int {
    val lowerChar = char.lowercaseChar()
    if (lowerChar < 'a' || lowerChar > 'z') {
        throw IllegalArgumentException("Invalid character requested.")
    }

    var priority = lowerChar - 'a' + 1
    if (char.isUpperCase()) { priority += 26 }

    return priority
}

fun oldMain() {
    var prioritySum = 0L
    File(INPUT_FILE).forEachLine { sack ->
        val firstCompartment = sack.substring(0, sack.length / 2)
        val secondCompartment = sack.substring(sack.length / 2)

        var found = false
        for (letter:Char in firstCompartment) {
            if (secondCompartment.contains(letter)) {
                prioritySum += getPriority(letter)
                found = true
                break
            }
        }
        if (!found) {
            throw RuntimeException("Unable to find an element that appears twice in '$sack'.")
        }
    }

    println("The sum of the priorities is: $prioritySum")
}
