package poklin.opponentmodeling

enum class ContextPlayers {
    FEW, MANY;

    companion object {
        fun valueFor(numberOfPlayersRemaining: Int): ContextPlayers {
            return if (numberOfPlayersRemaining < 3) {
                FEW
            } else {
                MANY
            }
        }
    }
}