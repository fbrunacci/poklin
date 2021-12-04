package poklin.opponentmodeling

import poklin.Player
import poklin.model.bet.BettingDecision
import poklin.model.bet.BettingRoundName

class ContextAction(
    val player: Player, val bettingDecision: BettingDecision, val bettingRoundName: BettingRoundName,
    numberOfRaises: Int, numberOfPlayersRemaining: Int, potOdds: Double
)