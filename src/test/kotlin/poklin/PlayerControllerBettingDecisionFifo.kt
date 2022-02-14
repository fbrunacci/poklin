package poklin

import poklin.controler.player.PlayerController
import poklin.model.bet.BettingDecision
import poklin.model.cards.Card
import java.util.*

internal class PlayerControllerBettingDecisionFifo : PlayerController() {
    var fifo = LinkedList<BettingDecision>()
    override fun decide(player: Player, currentGame: Game, cards: List<Card>): BettingDecision {
        if (fifo.isEmpty()) {
            throw RuntimeException("no more BettingDecision in fifo for player " + player.seat)
        }
        return fifo.removeFirst()
    }

    fun pushDecision(bettingDecision: BettingDecision) {
        fifo.add(bettingDecision)
    }
}