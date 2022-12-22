package problem04

import java.io.File

private const val INPUT_FILE = "input.txt"

fun main() {
    // The total number of lines where one elf's work has overlap with its peer.
    var totalOverlap = 0

    // Each line is expected to be of the form '99-99,18-99'.
    File(INPUT_FILE).forEachLine { line ->
        line.split(",").apply {
            var elf1: Pair<Int, Int>
            var elf2: Pair<Int, Int>

            // Obtain the integer values of each elf's range.
            this[0].split("-").apply { elf1 = Pair(this[0].toInt(), this[1].toInt()) }
            this[1].split("-").apply { elf2 = Pair(this[0].toInt(), this[1].toInt()) }


            val overlap = if (elf1.first == elf2.first || elf1.second == elf2.second) {
                // If either index is equal, one range will always encompass the
                // other.
                true
            }
            else if (elf1.first < elf2.first) {
                // When elf2's lower range is greater than elf1's, the overlap
                // is dependent on where elf1's upper bound ends.
                elf2.first <= elf1.second
            }
            else {
                elf1.first <= elf2.second
            }

            if (overlap) { totalOverlap += 1 }
        }
    }

    println("The total number of ranges with overlap is $totalOverlap.")
}
