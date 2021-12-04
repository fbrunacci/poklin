package poklin.opponentmodeling

enum class ContextPotOdds {
    LOW, HIGH;

    companion object {
        fun valueFor(potOdds: Double): ContextPotOdds {
            return if (potOdds > 0.2) {
                HIGH
            } else {
                LOW
            }
        }
    }
}