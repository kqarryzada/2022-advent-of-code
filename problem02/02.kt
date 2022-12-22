package problem02

import problem02.GameObject.*
import java.io.File

private const val INPUT_FILE = "input.txt"

private enum class GameObject {
    ROCK,
    PAPER,
    SCISSORS,
}

private val charToGameObject = mapOf(
        'A' to ROCK,
        'B' to PAPER,
        'C' to SCISSORS,
        'X' to ROCK,
        'Y' to PAPER,
        'Z' to SCISSORS,
)

/**
 * Determines the winner of a round of "Rock, Paper, Scissors".
 *
 * @param opponentActionChar    The character (A, B, C) referring to what the
 *                              opponent played this round.
 * @param playerActionChar      The character (X, Y, Z) referring to what the
 *                              player played this round.
 * @return  The player's score.
 */
fun determineWinner(opponentActionChar: Char, playerActionChar: Char): Int {
    var score: Int
    val opponentAction = charToGameObject[opponentActionChar]
    val playerAction = charToGameObject[playerActionChar]

    // The score is calculated based on what was played and the result of the
    // round. Start with the former.
    score = when(playerAction) {
        ROCK -> 1
        PAPER -> 2
        SCISSORS -> 3
        else -> {
            throw IllegalArgumentException("'determineWinner' received an invalid input:"
                    + " $playerActionChar")
        }
    }

    // Add the score from the result. Losses earn 0 points.
    if (opponentAction == playerAction) {
        score += 3
    }
    else if (opponentAction == ROCK && playerAction == PAPER ||
            opponentAction == PAPER && playerAction == SCISSORS ||
            opponentAction == SCISSORS && playerAction == ROCK) {
        score += 6
    }

    return score
}

fun main() {
    var totalScore = 0
    File(INPUT_FILE).forEachLine { line ->
        val gameRound = line.split(" ").map { it.first() }
        val roundScore = determineWinner(gameRound[0], gameRound[1])
        totalScore += roundScore
    }

    println("The total score is $totalScore.")
}
