package poklin.controler.player

import poklin.Game
import poklin.Player
import poklin.controler.PlayerController
import poklin.compose.state.PlayerState
import poklin.model.bet.BettingDecision
import poklin.model.bet.BettingDecision.BettingAction.*
import poklin.model.cards.Card

class AskPlayerController(val playerState: PlayerState) : PlayerController() {
    override fun toString(): String {
        return "ask"
    }

    override fun decide(player: Player, currentGame: Game, cards: List<Card>): BettingDecision {
        playerState.bettingDecision = NONE
        playerState.canCheck = currentGame.canCheck(player)
        // TODO
        // playerState.canCheck = true/false
        // playerState.minBet = xxx
        // playerState.maxBet = xxx
        playerState.waitForDecision = true // active l'affichage du choix

//        println("AskPlayerController waitForDecision " + Thread.currentThread().name.toString())
        var waitTime = 10000
        while (waitTime > 0 && playerState.waitForDecision) {
            Thread.sleep(500)
            waitTime -= 500
        }
        playerState.waitForDecision = false

//        println("AskPlayerController wakeup " + Thread.currentThread().name.toString())
        return when (playerState.bettingDecision) {
            CHECK -> BettingDecision.CHECK
            CALL -> BettingDecision.CALL
            RAISE -> BettingDecision.RAISE_MIN
            else -> BettingDecision.FOLD
        }
    }
}