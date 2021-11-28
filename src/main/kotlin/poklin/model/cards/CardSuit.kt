package poklin.model.cards

enum class CardSuit(private val symbol: String) {
    SPADE("\u2660"), HEART("\u2665"), CLUB("\u2663"), DIAMOND("\u2666");

    override fun toString(): String {
        return symbol
    }

    companion object {
        fun fromString(symbol: String): CardSuit {
            return when (symbol) {
                "s" -> SPADE
                "h" -> HEART
                "c" -> CLUB
                "d" -> DIAMOND
                else -> throw NoSuchElementException("No CardSuit found for $symbol")
            }
        }
    }
}