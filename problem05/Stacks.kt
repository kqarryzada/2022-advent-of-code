package problem05

import java.io.File
import java.util.Stack

// This is a matrix referring to the initial state of the crates. Note that the
// Stack at index 0 is empty and should not be used. It is populated so that the
// matrix can be used as if it is not zero-indexed.
//
// [M] [H]         [N]
// [S] [W]         [F]     [W] [V]
// [J] [J]         [B]     [S] [B] [F]
// [L] [F] [G]     [C]     [L] [N] [N]
// [V] [Z] [D]     [P] [W] [G] [F] [Z]
// [F] [D] [C] [S] [W] [M] [N] [H] [H]
// [N] [N] [R] [B] [Z] [R] [T] [T] [M]
// [R] [P] [W] [N] [M] [P] [R] [Q] [L]
//  1   2   3   4   5   6   7   8   9
private var crates: List<Stack<Char>> = mutableListOf()


private const val INPUT_FILE = "input.txt"


/**
 * Helper method that will create a Stack with a variable number of inputs.
 */
fun createStack(vararg inputs: Char): Stack<Char> {
    val newStack = Stack<Char>()
    inputs.forEach { newStack.add(it) }
    return newStack
}

private fun init() {
    crates = mutableListOf(
            createStack(),     // The zeroth index is not used.
            createStack('R', 'N', 'F', 'V', 'L', 'J', 'S', 'M'),
            createStack('P', 'N', 'D', 'Z', 'F', 'J','W', 'H'),
            createStack('W', 'R', 'C', 'D', 'G'),
            createStack('N', 'B', 'S'),
            createStack('M', 'Z', 'W', 'P', 'C', 'B', 'F', 'N'),
            createStack('P', 'R', 'M', 'W'),
            createStack('R', 'T', 'N', 'G', 'L', 'S', 'W'),
            createStack('Q', 'T', 'H', 'F', 'N', 'B', 'V'),
            createStack('L', 'M', 'H', 'Z', 'N', 'F'),
    )
}

private fun printSolution() {
    // Remove the empty zeroth element before printing.
    crates = crates.drop(1)

    println("The end program state:")
    println(crates)
    println()
    println("Solution input:")

    val builder = StringBuilder()
    for (stack in crates) {
        builder.append(stack.pop())
    }
    println(builder)
}

fun main() {
    init()

    File(INPUT_FILE).forEachLine { line ->
        val sourceStack: Int
        val targetStack: Int
        val amount: Int

        // Each line will be of the form:
        // move 15 from 6 to 5
        line.split(" ").apply {
            amount = this[1].toInt()
            sourceStack = this[3].toInt()
            targetStack = this[5].toInt()
        }

        if (crates[sourceStack].size < amount) { throw RuntimeException("Not enough elements to move.") }

        // Move the crates from the source stack to the target stack. To preserve the order, first
        // place the contents to a temporary stack.
        val tempStack = Stack<Char>()
        for (i in 1..amount) {
            val topCrate = crates[sourceStack].pop()
            tempStack.push(topCrate)
        }
        for(i in 1..amount) {
            crates[targetStack].push(tempStack.pop())
        }
    }

    printSolution()
}
