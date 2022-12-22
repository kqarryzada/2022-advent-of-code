package problem01

import java.io.File

private const val INPUT_FILE = "input.txt"

// A list of the three largest values computed. The 0th index contains the
// largest value.
private var max = mutableListOf<Long>(0, 0, 0)
private var current = 0L

/**
 * Updates the maximum values based on the new data. This method always clears
 * the 'current' counter.
 */
fun update() {
    if (current > max[0]) {
        max[2] = max[1]
        max[1] = max[0]
        max[0] = current
    }
    else if (current > max[1]) {
        max[2] = max[1]
        max[1] = current
    }
    else if (current > max[2]) {
        max[2] = current
    }

    // Clear the counter for the next iteration.
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

    println("The three highest totals were '$max'.")
    println("The sum was '${max.sum()}'.")
}
