package problem09

import java.io.File
import kotlin.math.absoluteValue

private const val INPUT_FILE = "input.txt"

// A set of coordinates indicating unique locations that the tail has resided.
private val tailHistory = mutableSetOf<Coordinate>()

private var head = Coordinate(0, 0)
private var tail = Coordinate(0, 0)

/**
 * A data type that refers to a coordinate on the grid (e.g., `(0, 0)`).
 */
class Coordinate() {
    var x: Int = 0
    var y: Int = 0

    constructor(x: Int, y: Int) : this() {
        this.x = x
        this.y = y
    }

    override fun equals(other: Any?): Boolean {
        return other is Coordinate && other.x == x && other.y == y
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}


/**
 * Updates the tail's coordinate based on the current position of the head. This
 * method should be called every time the tail is updated.
 */
fun updateTail() {
    val xDiff = head.x - tail.x
    val yDiff = head.y - tail.y

    if (xDiff.absoluteValue <= 1 && yDiff.absoluteValue <= 1) {
        // The tail does not need to be updated.
        return
    }

    if (xDiff != 0) {
        tail.x += if (xDiff > 0) 1 else -1
    }
    if (yDiff != 0) {
        tail.y += if (yDiff > 0) 1 else -1
    }

    tailHistory.add(Coordinate(tail.x, tail.y))
}

fun part1() {
    File(INPUT_FILE).forEachLine { line ->
        val args = line.split(" ")
        if (args.size != 2) { throw RuntimeException("Invalid input: $line") }

        for (i in 1..args[1].toInt()) {
            when (args[0]) {
                "R" -> { head.x++ }

                "L" -> { head.x-- }

                "U" -> { head.y++ }

                "D" -> { head.y-- }

                else -> {
                    throw RuntimeException("Invalid direction: ${args[0]}")
                }
            }
            updateTail()
        }
    }

    println("The tail visited ${tailHistory.size} unique positions.")
}

fun main() {
    part1()
}
