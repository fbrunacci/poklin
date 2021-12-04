package poklin.controler.phase1

import poklin.GameHand
import poklin.Player
import poklin.HandPowerRanker
import poklin.dependencyinjection.PlayerControllerPrePost
import poklin.model.HandPowerType
import poklin.model.bet.BettingDecision
import poklin.model.cards.Card
import javax.inject.Inject

class PlayerControllerPhaseIBluff : PlayerControllerPrePost() {
    override fun toString(): String {
        return "PhaseI bluff"
    }

    public override fun decidePreFlop(
        player: Player, gameHand: GameHand,
        cards: List<Card>
    ): BettingDecision {
        val card1 = cards!![0]
        val card2 = cards[1]
        val sumPower = card1.number.power + card2.number.power
        return if (card1.number == card2.number || sumPower <= 8) {
            BettingDecision.RAISE_MIN
        } else {
            if (sumPower > 16 || canCheck(gameHand, player)) {
                BettingDecision.CALL
            } else {
                BettingDecision.FOLD
            }
        }
    }

    public override fun decideAfterFlop(
        player: Player, gameHand: GameHand,
        cards: List<Card>
    ): BettingDecision {
        val handPower = HandPowerRanker.rank(cards)
        val handPowerType = handPower.handPowerType
        return if (handPowerType == HandPowerType.HIGH_CARD) {
            BettingDecision.RAISE_MIN
        } else if (handPowerType.power >= HandPowerType.STRAIGHT.power) {
            BettingDecision.RAISE_MIN
        } else {
            if (canCheck(gameHand, player)) {
                BettingDecision.CALL
            } else BettingDecision.FOLD
        }
    }
}