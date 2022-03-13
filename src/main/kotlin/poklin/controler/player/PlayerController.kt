package poklin.controler.player

import poklin.Game
import poklin.Player
import poklin.compose.state.TableState
import poklin.model.bet.BettingDecision
import poklin.model.cards.Card

abstract class PlayerController {
    fun doDecide(player: Player, game: Game): BettingDecision {
        val cards: MutableList<Card> = ArrayList()
        cards.addAll(game.sharedCards)
        cards.addAll(player.holeCards!!)

        val playerState = player.playerState!!
        playerState.bettingDecision = BettingDecision.BettingAction.NONE
        playerState.canCheck = game.canCheck(player)
        playerState.progress = 1.0f

        playerState.minBettingAmount = minOf(playerState.money , maxOf(game.tableState.highestBet() - playerState.bettingAmount, 20f))
        playerState.sliderBettingState = playerState.minBettingAmount

        // TODO
        // playerState.canCheck = true/false
        // playerState.maxBet = xxx // TODO cas ou autre jouer allin avec moins d argent

        playerState.waitForDecision = true // active l'affichage du choix
        val decide = decide(player, game, cards)
        playerState.waitForDecision = false
        return decide
    }

    protected abstract fun decide(player: Player, currentGame: Game, cards: List<Card>): BettingDecision
}