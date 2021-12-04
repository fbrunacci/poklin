package poklin.dependencyinjection.preflopsim

import poklin.GameHand
import poklin.HandPowerRanker
import poklin.Player
import poklin.dependencyinjection.PlayerControllerPrePost
import poklin.model.HandPowerType
import poklin.model.bet.BettingDecision
import poklin.model.cards.Card

/**
 * A naive player that cannot fold but only bet. Used during pre flop rollout
 * simulations
 */
class PlayerControllerPreFlopRoll : PlayerControllerPrePost() {
    override fun toString(): String {
        return "Preflop"
    }

    public override fun decidePreFlop(
        player: Player, gameHand: GameHand,
        cards: List<Card>
    ): BettingDecision {
        val card1 = cards!![0]
        val card2 = cards[1]
        return if (card1.number == card2.number) {
            BettingDecision.RAISE_MIN
        } else if (card1.number.power + card2.number.power > 16
            || canCheck(gameHand, player)
        ) {
            BettingDecision.CALL
        } else {
            BettingDecision.CALL
        }
    }

    public override fun decideAfterFlop(player: Player, gameHand: GameHand, cards: List<Card>): BettingDecision {
        val handPower = HandPowerRanker.rank(cards)
        val handPowerType = handPower.handPowerType
        return if (handPowerType == HandPowerType.HIGH_CARD) {
            if (canCheck(gameHand!!, player)) {
                BettingDecision.CALL
            } else BettingDecision.CALL
        } else if (handPowerType.power >= HandPowerType.THREE_OF_A_KIND.power) {
            BettingDecision.RAISE_MIN
        } else {
            BettingDecision.CALL
        }
    }
}