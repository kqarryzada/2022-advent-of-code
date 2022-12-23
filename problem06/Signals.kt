package problem06

import java.io.File

private const val INPUT_FILE = "input.txt"

fun main() {
    // The file's contents are stored on a single line.
    val input = File(INPUT_FILE).readLines()[0]

    var index = 0
    var elements = mutableListOf<Char>()
    for (char in input) {
        if (elements.contains(char)) {
            // A repeated character was detected, so we must start over from that position.
            val startingIndex = elements.indexOf(char)
            elements = elements.subList(startingIndex + 1, elements.size)
        }
        else if (elements.size == 13) {
            // We have found the final non-repeating character.
            elements.add(char)
            index++
            break
        }

        elements.add(char)
        index++
    }

    println("The first time a start-of-message marker appears is after character number '$index'. This refers to:")
    println(String(elements.toCharArray()))
}
