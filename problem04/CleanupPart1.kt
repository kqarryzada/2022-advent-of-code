package problem04

import java.io.File

private const val INPUT_FILE = "input.txt"

fun main() {
    // The total number of lines where one elf's work is completely contained by
    // its peer.
    var totalContained = 0

    // Each line is expected to be of the form '99-99,18-99'.
    File(INPUT_FILE).forEachLine { line ->
        line.split(",").apply {
            var elf1: Pair<Int, Int>
            var elf2: Pair<Int, Int>

            // Obtain the integer values of each elf's range.
            this[0].split("-").apply { elf1 = Pair(this[0].toInt(), this[1].toInt()) }
            this[1].split("-").apply { elf2 = Pair(this[0].toInt(), this[1].toInt()) }


            val contained = if (elf1.first == elf2.first || elf1.second == elf2.second) {
                // If either index is equal, one range will always encompass the
                // other.
                true
            }
            else if (elf1.first <= elf2.first) {
                elf2.second <= elf1.second
            }
            else {
                // In this case, the second elf's lower range (i.e., "first") is
                // always less than its peer.
                elf1.second <= elf2.second
            }

            if (contained) { totalContained += 1 }
        }
    }

    println("The total number of 'contained' ranges is $totalContained.")
}
