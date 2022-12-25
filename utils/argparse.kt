package utils

private val partOneFlags = setOf("1", "part1")
private val partTwoFlags = setOf("2", "part2")


/**
 * Indicates whether part 1 or 2 of the problem should be executed. This is
 * used in the `main()` method when both parts of a day's problems are closely
 * coupled.
 *
 * @return `true` if part 1 should be executed, or `false` if part 2 should be executed.
 * @throws RuntimeException  If an invalid argument is provided.
 */
fun shouldRunPart1(args: Array<String>): Boolean {
    if (args.isEmpty() || partOneFlags.contains(args[0])) { return true }
    if (partTwoFlags.contains(args[0])) { return false }

    throw RuntimeException("Invalid flag '${args[0]}' provided.")
}
