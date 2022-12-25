package problem07

import utils.shouldRunPart1
import java.io.File
import java.util.Stack

private const val INPUT_FILE = "input.txt"

// The threshold for determining whether a directory should be included in the
// output sum.
private const val THRESHOLD_BYTES = 100_001L

// The folder that is to be deleted must be at least this many bytes in size.
private const val FREE_SPACE_REQUIRED = 30_000_000L

private const val TOTAL_FILE_SYSTEM_SIZE = 70_000_000L

// The buffered reader that will allow us to iterate through the bash history
// given by 'INPUT_FILE'.
private val reader = File(INPUT_FILE).bufferedReader()

// Tracks the size data for directories smaller than the 'THRESHOLD_BYTES'.
private val directorySizeData = mutableMapOf<String, Long>()

// Contains the full filepath for the directory we are currently within.
private val currentPath = Stack<String>()


/**
 * Updates the directory content of `currentPath`.
 */
private fun cd(folder: String?) {
    // In UNIX or Linux, 'cd' with no arguments moves to the HOME directory, but
    // this is not the case for this problem.
    folder ?: throw RuntimeException("Tried to 'cd', but a folder was not specified.")

    if (folder == ".." && currentPath.peek() != "/") {
        // Move to the parent folder. The root folder's parent is itself.
        currentPath.pop()
        return
    }

    currentPath.push(folder)

    // Initialize new folders with 0 bytes of data.
    var fullPath = ""
    currentPath.forEach { fullPath += it }
    directorySizeData.putIfAbsent(fullPath, 0)
}

/**
 * Iterates through the output of an `ls` command and updates the contents of
 * `directorySizeData`. This function returns the next command from the input
 * file, or `null` if there are no commands left to run.
 */
private fun lsAndReturnNextCommand(): String? {
    // Iterate through the input data until we reach the next command, which
    // begins with '$'.
    var filesystemLine = reader.readLine() ?: return null
    while (!filesystemLine.startsWith("$")) {
        val args = filesystemLine.split(" ")

        // There are two forms for the commands: 'dir <dirname>' and '<filesize> <filename>'.
        if (args[0] != "dir") {
            // Update the size for each folder in the path.
            val fileSize = args[0].toLong()
            var fullPath = ""
            currentPath.forEach {
                fullPath += it
                val newSize = (directorySizeData[fullPath] ?: 0) + fileSize
                directorySizeData[fullPath] = newSize
            }
        }

        filesystemLine = reader.readLine() ?: return null
    }

    // We have already extracted the next command, so return it.
    return filesystemLine
}

/**
 * Navigates through a bash history file and collects directory size
 * information, which is stored in `directorySizeData`.
 */
private fun analyzeHistory() {
    var line = reader.readLine()

    while (line != null) {
        if (line.startsWith("$ cd")) {
            cd(line.split(" ")[2])
            line = reader.readLine()
        }
        else if (line == "$ ls") {
            line = lsAndReturnNextCommand()
        }
        else { throw RuntimeException("Invalid command detected: '$line'") }
    }
    reader.close()
}

fun main(args: Array<String>) {
    val part1 = shouldRunPart1(args)
    analyzeHistory()

    if (part1) {
        var sumUnderThreshold = 0L
        directorySizeData.filter { it.value < THRESHOLD_BYTES }.forEach { sumUnderThreshold += it.value }
        // println("Debug: The map contains: $directorySizeData")

        println("The sum of directories under the the threshold ($THRESHOLD_BYTES) is $sumUnderThreshold bytes.")
    }
    else {
        // Find the amount of existing data we need to free.
        val rootSize = directorySizeData["/"] ?: throw RuntimeException()
        val diskSpaceNeeded = FREE_SPACE_REQUIRED - (70000000 - rootSize)
        val smallestCandidateDirectory = directorySizeData.values.filter { it > diskSpaceNeeded }.min()

        println("The best candidate directory has a size of $smallestCandidateDirectory bytes.")
    }
}
