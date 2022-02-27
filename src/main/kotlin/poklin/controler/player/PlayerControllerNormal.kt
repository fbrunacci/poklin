package poklin.controler.player

import poklin.Game
import poklin.Player
import poklin.HandPowerRanker
import poklin.model.HandPowerType
import poklin.model.bet.BettingDecision
import poklin.model.cards.Card

class PlayerControllerNormal : AbstractPlayerController() {
    override fun toString(): String {
        return "normal"
    }

    public override fun decidePreFlop(
        player: Player, game: Game,
        cards: List<Card>
    ): BettingDecision {
        val card1 = cards!![0]
        val card2 = cards[1]
        return if (card1.number == card2.number) {
            BettingDecision.RAISE_MIN
        } else if (card1.number.power + card2.number.power > 16
            || canCheck(game, player)
        ) {
            BettingDecision.CALL
        } else {
            BettingDecision.FOLD
        }
    }

    public override fun decideAfterFlop(player: Player, game: Game, cards: List<Card>): BettingDecision {
        val handPower = HandPowerRanker.rank(cards)
        val handPowerType = handPower.handPowerType
        return if (handPowerType == HandPowerType.HIGH_CARD) {
            if (canCheck(game!!, player)) {
                BettingDecision.CALL
            } else BettingDecision.FOLD
        } else if (handPowerType.power >= HandPowerType.STRAIGHT.power) {
            BettingDecision.RAISE_MIN
        } else {
            BettingDecision.CALL
        }
    }
}