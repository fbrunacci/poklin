package poklin.controler

import poklin.Game
import poklin.Player
import poklin.model.bet.BettingDecision
import poklin.model.cards.Card

abstract class PlayerController {
    fun doDecide(player: Player, game: Game): BettingDecision {
        val cards: MutableList<Card> = ArrayList()
        cards.addAll(game.sharedCards)
        cards.addAll(player.holeCards!!)
        return decide(player, game, cards)
    }

    protected abstract fun decide(player: Player, currentGame: Game, cards: List<Card>): BettingDecision
}