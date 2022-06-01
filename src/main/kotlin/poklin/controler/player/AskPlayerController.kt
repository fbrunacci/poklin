package poklin.controler.player

import poklin.Game
import poklin.Player
import poklin.model.bet.BettingDecision
import poklin.model.bet.BettingDecision.BettingAction.*
import poklin.model.cards.Card

class AskPlayerController(val totalWaitTime: Int = 10000) : PlayerController() {
    override fun toString(): String {
        return "ask"
    }

    override fun decide(player: Player, currentGame: Game, cards: List<Card>): BettingDecision {
        val playerState = player.playerState!!

        var waitTime = 0
        while (waitTime <= totalWaitTime && playerState.waitForDecision) {
            Thread.sleep(500)
            waitTime += 500
            playerState.progress = 1 - (waitTime / totalWaitTime.toFloat())
        }

//        var p = 0
//        while (playerState.waitForDecision) {
//            playerState.progress = 1 - (p.toFloat() / 100f)
//            Thread.sleep(500)
//            p++
//        }


        return when (playerState.bettingDecision) {
            CHECK -> BettingDecision.CHECK
            CALL -> BettingDecision.CALL
            RAISE -> BettingDecision.RAISE_CUSTOM(playerState.bettingAmount)
            else -> BettingDecision.FOLD
        }
    }
}