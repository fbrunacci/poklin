package poklin.controler.player

import poklin.Game
import poklin.Player
import poklin.model.bet.BettingDecision
import poklin.model.bet.BettingDecision.BettingAction.*
import poklin.model.cards.Card

class AskPlayerController() : PlayerController() {
    override fun toString(): String {
        return "ask"
    }

    override fun decide(player: Player, currentGame: Game, cards: List<Card>): BettingDecision {
        val playerState = player.playerState!!

        val totalWaitTime = 10000
        var waitTime = totalWaitTime
        while (waitTime > 0 && playerState.waitForDecision) {
            Thread.sleep(500)
            waitTime -= 500
            playerState.progress = (waitTime.toFloat() / totalWaitTime.toFloat())
        }


        return when (playerState.bettingDecision) {
            CHECK -> BettingDecision.CHECK
            CALL -> BettingDecision.CALL
            RAISE -> BettingDecision.RAISE_CUSTOM(playerState.bettingAmount)
            else -> BettingDecision.FOLD
        }
    }
}