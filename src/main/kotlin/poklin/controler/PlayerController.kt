package poklin.controler

import poklin.GameHand
import poklin.Player
import poklin.model.bet.BettingDecision
import poklin.model.cards.Card

abstract class PlayerController {
    fun doDecide(player: Player, gameHand: GameHand): BettingDecision {
        val cards: MutableList<Card> = ArrayList()
        cards.addAll(gameHand.sharedCards)
        cards.addAll(player.holeCards!!)
        return decide(player, gameHand, cards)
    }

    protected abstract fun decide(player: Player?, gameHand: GameHand?, cards: List<Card>?): BettingDecision
}