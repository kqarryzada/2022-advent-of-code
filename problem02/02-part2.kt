package problem02

import problem02.GameObject.*
import java.io.File

private const val INPUT_FILE = "input.txt"

private val charToGameObject = mapOf(
        'A' to ROCK,
        'B' to PAPER,
        'C' to SCISSORS,
)

/**
 * Determines the player's score after a round of "Rock, Paper, Scissors".
 *
 * @param opponentActionChar    The character (A, B, C) referring to what the
 *                              opponent played this round.
 * @param playerResultChar      The character (X, Y, Z) referring to the
 *                              player's desired result for this round.
 * @return  The player's score.
 */
private fun determineScore(opponentActionChar: Char, playerResultChar: Char): Int {
    val opponentAction = charToGameObject[opponentActionChar]
            ?: throw IllegalArgumentException("'determineWinner' received an invalid input:" +
                    " $opponentActionChar")

    // Determine the appropriate action the player should use based on the
    // desired result. For example, if the opponent plays "Scissors", and the
    // desired result is a loss (i.e., playerResult == `X`), the player should
    // use `GameObject.PAPER`. The player would get zero points due to the loss.
    var score: Int
    val playerAction = when(playerResultChar) {
        // Win
        'Z' -> {
            score = 6
            when(opponentAction) {
                SCISSORS -> ROCK
                ROCK -> PAPER
                else -> SCISSORS
            }
        }

        // Lose
        'X' -> {
            score = 0
            when(opponentAction) {
                ROCK -> SCISSORS
                PAPER -> ROCK
                else -> PAPER
            }
        }

        // Draw
        'Y' -> {
            score = 3
            opponentAction
        }

        else -> {
            throw IllegalArgumentException("'determineWinner' received an invalid input:"
                    + " $playerResultChar")
        }
    }

    // The score is also based on the action that the player used this turn.
    score += when(playerAction) {
        ROCK -> 1
        PAPER -> 2
        SCISSORS -> 3
    }

    return score
}

fun main() {
    var totalScore = 0
    File(INPUT_FILE).forEachLine { line ->
        val opponentAction: Char
        val desiredResult: Char
        line.split(" ").map { it.first() }.apply {
            opponentAction = this[0]
            desiredResult = this[1]
        }

        val roundScore = determineScore(opponentAction, desiredResult)
        totalScore += roundScore
    }

    println("The total score is $totalScore.")
}
