package problem06

import java.io.File

private const val INPUT_FILE = "input.txt"

fun oldMain() {
    // The file's contents are stored on a single line.
    val input = File(INPUT_FILE).readLines()[0]

    var index = 0
    var elements = mutableListOf<Char>()
    for (char in input) {
        if (elements.isEmpty()) {
            elements.add(char)
            index++
            continue
        }

        if (elements.contains(char)) {
            // A repeated character was detected, so we must start over from this position.
            elements = mutableListOf(char)
        }
        else if (elements.size < 3) {
            elements.add(char)
        }
        else {
            // We have found a fourth non-repeating character.
            elements.add(char)
            index++
            break
        }
        index++
    }

    println("The first time a marker appears is after character number '$index'. This refers to:")
    println(String(elements.toCharArray()))
}
