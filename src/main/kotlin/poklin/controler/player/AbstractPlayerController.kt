package poklin.controler.player

import poklin.Game
import poklin.Player
import poklin.model.bet.BettingDecision
import poklin.model.bet.BettingRoundName
import poklin.model.cards.Card

abstract class AbstractPlayerController : PlayerController() {
    public override fun decide(
        player: Player, currentGame: Game,
        cards: List<Card>
    ): BettingDecision {
        return if (currentGame.bettingRoundName == BettingRoundName.PRE_FLOP) {
            decidePreFlop(player, currentGame, cards)
        } else {
            decideAfterFlop(player, currentGame, cards)
        }
    }

    protected fun canCheck(game: Game, player: Player): Boolean {
        val bettingRound = game.currentBettingRound
        return bettingRound.highestBet == bettingRound.getBetForPlayer(player)
    }

    protected abstract fun decidePreFlop(player: Player, game: Game, cards: List<Card>): BettingDecision
    protected abstract fun decideAfterFlop(player: Player, game: Game, cards: List<Card>): BettingDecision
}