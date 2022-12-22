package problem01

import java.io.File

private const val INPUT_FILE = "input.txt"

private var max = 0L
private var current = 0L

/**
 * Updates the maximum value if applicable, and clears the 'current' counter.
 */
fun update() {
    max = if (current > max) current else max
    current = 0
}


fun main() {
    File(INPUT_FILE).forEachLine {line ->
        if (line.isEmpty()) {
            update()
        }
        else {
            current += line.toLong()
        }
    }
    // Check the sum of the final group of numbers.
    update()

    println("The maximum sum was '$max'.")
}
