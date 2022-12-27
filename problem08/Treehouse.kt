package problem08

import java.io.File
import problem08.Visibility.*
import utils.shouldRunPart1
import java.util.*

private const val INPUT_FILE = "input.txt"

// A matrix consisting of the tree heights provided in the input file.
private val heightMatrix = mutableListOf<List<Int>>()

// A matrix denoting whether trees in the height matrix are "visible" or not.
private val visibleMatrix = mutableListOf<MutableList<Visibility>>()

// A matrix consisting of the "scenic score" of each tree in the height matrix.
private val scenicMatrix = mutableListOf<MutableList<Long>>()


// Indicates the number of elements in a row or column of the visibility matrix
// and the height matrix.
private var matrixLength = 0


enum class Visibility {
    UNKNOWN,
    VISIBLE,
    NOT_VISIBLE,
}

fun printMatrix() {
    visibleMatrix.forEach { row ->
        println(row.map {
            when (it) {
                VISIBLE -> { "VIS" }
                NOT_VISIBLE -> { "NOT" }
                else -> { "UNK" }
            }
        })
    }
}

/**
 * Stores the numerical values of `INPUT_FILE` into matrix form. This method
 * initializes all of the matrices.
 */
private fun parseMatrixFromFile() {
    // Before iterating, grab the first line to evaluate the number of elements
    // in a row.
    val initialReader = File(INPUT_FILE).bufferedReader()
    matrixLength = initialReader.readLine().length
    initialReader.close()


    File(INPUT_FILE).forEachLine { line ->
        // Parse the line of text as individual numbers in a row of the height
        // matrix.
        val list = mutableListOf<Int>()
        line.forEach { list.add(it.digitToInt()) }
        heightMatrix.add(list)

        visibleMatrix.add(MutableList(matrixLength) { UNKNOWN })
        scenicMatrix.add(MutableList(matrixLength) { -1 })
    }

    // Initialize the visibility matrix. All trees on the edges of the graph are
    // considered visible.
    visibleMatrix[0] = MutableList(matrixLength) { VISIBLE }
    visibleMatrix[visibleMatrix.lastIndex] = MutableList(matrixLength) { VISIBLE }
    visibleMatrix.forEach { row ->
        row[0] = VISIBLE
        row[row.lastIndex] = VISIBLE
    }

    // Initialize the scenic matrix. All edge trees will have a score of 0.
    scenicMatrix[0] = MutableList(matrixLength) { 0 }
    scenicMatrix[scenicMatrix.lastIndex] = MutableList(matrixLength) { 0 }
    scenicMatrix.forEach { row ->
        row[0] = 0
        row[row.lastIndex] = 0
    }
}

/**
 * Populates the contents of `visibleMatrix` with an indication of whether a
 * tree can be viewed from one of the edges of the grid. See the problem
 * description for more information.
 */
private fun createVisibilityMap() {
    val numberOfColumns = visibleMatrix[0].lastIndex

    // This list keeps track of the tallest trees from the perspective of someone looking from the
    // north. Initialize this with a copy of the top row, as the first row of trees will always be
    // visible.
    val tallestTreesNorth = heightMatrix[0].toMutableList()

    for (rowNumber in 1..visibleMatrix.lastIndex) {
        // The tallest tree for this row, from the perspective of someone looking from the west.
        var tallestTreeWest = heightMatrix[rowNumber][0]

        // Iterate over the matrix and consider visibility from the north and west. The other two
        // perspectives cannot be evaluated yet, since it would require iterating over data that
        // is not yet parsed.
        for (columnNumber in 1 until numberOfColumns) {
            var isVisible = (visibleMatrix[rowNumber][columnNumber] == VISIBLE)
            val treeHeight = heightMatrix[rowNumber][columnNumber]
            val tallestNorth = tallestTreesNorth[columnNumber]

            if (treeHeight > tallestNorth) {
                // This tree is visible from the north.
                isVisible = true
                tallestTreesNorth[columnNumber] = treeHeight
            }
            if (treeHeight > tallestTreeWest) {
                isVisible = true
                tallestTreeWest = treeHeight
            }

            // Mark short trees as "not visible" for now. They can be overridden when evaluating
            // the east and south perspectives.
            visibleMatrix[rowNumber][columnNumber] = if (isVisible) VISIBLE else NOT_VISIBLE
        }

        // Iterate through the row again, in reverse, to evaluate visibility from the east. Start
        // with the second to last tree, as the last tree will always be visible.
        var tallestTreeEast = heightMatrix[rowNumber][numberOfColumns]
        val tallestTreeInRow = tallestTreeWest
        for (columnNumber in numberOfColumns - 1 downTo 1) {
            val treeHeight = heightMatrix[rowNumber][columnNumber]
            if (treeHeight > tallestTreeEast) {
                visibleMatrix[rowNumber][columnNumber] = VISIBLE
                tallestTreeEast = treeHeight

                // 'tallestTreeWest' contains the largest known tree height in this row.
                if (treeHeight == tallestTreeInRow) {
                    // Anything to the left of this tree will not be visible from the east/right.
                    break
                }
            }
        }
    }

    // Perform one final pass for visibility from the south. Start with the second-to-last row,
    // going upwards until we reach the tallest tree in the column.
    for (columnNumber in 1 until numberOfColumns) {
        var tallestTreeSouth = heightMatrix[visibleMatrix.lastIndex][columnNumber]
        val tallestTreeInColumn = tallestTreesNorth[columnNumber]
        for (rowNumber in visibleMatrix.lastIndex - 1 downTo 1) {
            val treeHeight = heightMatrix[rowNumber][columnNumber]
            if (treeHeight > tallestTreeSouth) {
                visibleMatrix[rowNumber][columnNumber] = VISIBLE
                tallestTreeSouth = treeHeight
            }

            if (treeHeight == tallestTreeInColumn) {
                // If this tree is one of the tallest trees in the column, then anything north
                // of this tree will not be visible from the south.
                break
            }
        }
    }
}

/**
 * Calculates the scenic product for a list of tree heights. This may refer to
 * either a row or column in the height matrix.
 *
 * This function returns a list of scenic scores for every tree in the provided
 * height list. This is slightly more efficient than repeatedly invoking this
 * function for each element in the height list.
 */
private fun scenicProduct(heightList: List<Int>): MutableList<Long> {
    val productList = mutableListOf<Long>()

    // The scenic score of edges is always zero.
    productList.add(0)

    for (treeIndex in 1 until heightList.lastIndex) {
        val treeHeight = heightList[treeIndex]
        var leftScenicScore = 0L
        var rightScenicScore = 0L

        for (peerIndex in treeIndex - 1 downTo 0) {
            leftScenicScore++
            val peerHeight = heightList[peerIndex]
            if (peerHeight >= treeHeight) {
                // A taller tree has been found.
                break
            }
        }

        for (peerIndex in treeIndex + 1..heightList.lastIndex) {
            rightScenicScore++
            val peerHeight = heightList[peerIndex]
            if (peerHeight >= treeHeight) {
                break
            }
        }

        productList.add(leftScenicScore * rightScenicScore)
    }

    // Add the scenic score of the final edge.
    productList.add(0)
    return productList
}

/**
 * Calculates a "scenic score" for each tree based on the number of neighboring
 * trees that it can see in four directions (e.g., north). This function
 * populates the `scenicMatrix`.
 */
private fun calculateScenicScore() {
    val matrixLength = scenicMatrix[0].size

    for (rowNumber in 1 until matrixLength) {
        val rowList = heightMatrix[rowNumber]
        scenicMatrix[rowNumber] = scenicProduct(rowList)
    }

    for (columnNumber in 1 until matrixLength) {
        // Convert the column data into a list.
        val columnList = MutableList(matrixLength) { -1 }
        for (i in 0 until matrixLength) { columnList[i] = heightMatrix[i][columnNumber] }

        val scenicProductColumn = scenicProduct(columnList)
        for (i in 0 until matrixLength) {
            scenicMatrix[i][columnNumber] *= scenicProductColumn[i]
        }
    }
}

fun part1() {
    parseMatrixFromFile()
    createVisibilityMap()

    var visibleCount = 0
    visibleMatrix.forEach { row ->
        val rowVisibility = Collections.frequency(row, VISIBLE)
        visibleCount += rowVisibility
    }

    println("There are $visibleCount visible trees.")
}

fun part2() {
    parseMatrixFromFile()
    calculateScenicScore()

    var maxScenicScore = 0L
    scenicMatrix.forEach { row ->
        if (row.max() > maxScenicScore) { maxScenicScore = row.max() }
    }

    println("The largest scenic score in the grid is $maxScenicScore.")
}

fun main(args: Array<String>) {
    if (shouldRunPart1(args)) { part1() }
    else { part2() }
}
