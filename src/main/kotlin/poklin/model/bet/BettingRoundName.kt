package poklin.model.bet

enum class BettingRoundName {
    PRE_FLOP, POST_FLOP, POST_TURN, POST_RIVER;

    companion object {
        @JvmStatic
        fun fromRoundNumber(bettingRoundNumber: Int): BettingRoundName {
            return when (bettingRoundNumber) {
                2 -> POST_FLOP
                3 -> POST_TURN
                4 -> POST_RIVER
                else -> PRE_FLOP
            }
        }
    }
}