package poklin

import poklin.model.bet.BettingDecision.BettingAction

class Bet(var action: BettingAction, var amount: Float = 0f)