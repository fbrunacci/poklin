package poklin.dependencyinjection

import poklin.GameHand
import poklin.Player
import poklin.controler.PlayerController
import poklin.model.bet.BettingDecision
import poklin.model.bet.BettingRoundName
import poklin.model.cards.Card

abstract class PlayerControllerPrePost : PlayerController() {
    public override fun decide(
        player: Player, gameHand: GameHand,
        cards: List<Card>
    ): BettingDecision {
        return if (gameHand!!.bettingRoundName == BettingRoundName.PRE_FLOP) {
            decidePreFlop(player, gameHand, cards)
        } else {
            decideAfterFlop(player, gameHand, cards)
        }
    }

    protected fun canCheck(gameHand: GameHand, player: Player?): Boolean {
        val bettingRound = gameHand.currentBettingRound
        return bettingRound.highestBet == bettingRound.getBetForPlayer(player!!)
    }

    protected abstract fun decidePreFlop(player: Player, gameHand: GameHand, cards: List<Card>): BettingDecision
    protected abstract fun decideAfterFlop(player: Player, gameHand: GameHand, cards: List<Card>): BettingDecision
}