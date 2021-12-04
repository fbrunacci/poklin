package poklin.opponentmodeling

enum class ContextRaises {
    FEW, MANY;

    companion object {
        fun valueFor(numberOfRaises: Int): ContextRaises {
            return if (numberOfRaises < 3) {
                FEW
            } else {
                MANY
            }
        }
    }
}