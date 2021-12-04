package poklin.controler.player

import poklin.GameHand
import poklin.Player
import poklin.controler.PlayerController
import poklin.model.bet.BettingDecision
import poklin.model.cards.Card
import java.lang.IllegalArgumentException
import java.util.*

class AskPlayerController : PlayerController() {
    override fun toString(): String {
        return "ask"
    }

    override fun decide(player: Player, gameHand: GameHand, cards: List<Card>): BettingDecision {
        val reader = Scanner(System.`in`)
        println("Enter a number (0 FOLD, 1 CHECK, 2 CALL, 3 RAISE MIN, 4 RAISE POT, 5 ALLIN): ")
        do {
            val n = reader.nextInt() // Scans the next token of the input as an int.
            when(n) {
                0 -> return BettingDecision.FOLD
                1 -> return BettingDecision.CHECK
                2 -> return BettingDecision.CALL
                3 -> return BettingDecision.RAISE_MIN
                4 -> return BettingDecision.RAISE_POT
                5 -> return BettingDecision.RAISE_ALLIN
            }
        } while (!(1<=n && n<=5))
        throw IllegalArgumentException("Wrong choice")
    }
}