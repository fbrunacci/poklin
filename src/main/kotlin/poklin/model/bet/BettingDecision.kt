package poklin.model.bet

class BettingDecision(val bettingAction: BettingAction, var raiseAmount: RaiseAmount? = null, var amount: Float? = null) {

    enum class BettingAction {
        NONE, SMALLBLIND, BIGBLIND,
        CHECK, CALL, FOLD, RAISE
    }

    enum class RaiseAmount {
        RAISE_MIN, RAISE_POT, RAISE_ALLIN, RAISE_CUSTOM
    }

    companion object {
        @JvmStatic
        val FOLD = BettingDecision(BettingAction.FOLD)
        @JvmStatic
        val CHECK = BettingDecision(BettingAction.CHECK)
        @JvmStatic
        val CALL = BettingDecision(BettingAction.CALL)
        @JvmStatic
        val RAISE_POT = BettingDecision(BettingAction.RAISE, RaiseAmount.RAISE_POT)
        @JvmStatic
        val RAISE_ALLIN = BettingDecision(BettingAction.RAISE, RaiseAmount.RAISE_ALLIN)
        @JvmStatic
        val RAISE_MIN = BettingDecision(BettingAction.RAISE, RaiseAmount.RAISE_MIN)
        @JvmStatic
        fun RAISE_CUSTOM(amount: Float): BettingDecision {
            return BettingDecision(BettingAction.RAISE, RaiseAmount.RAISE_CUSTOM, amount)
        }
    }

    override fun toString(): String {
        return bettingAction.toString()
    }
}