package poklin.model.cards

enum class CardSuit(val text: String, val symbol: String) {
    SPADE("s", "\u2660"),
    HEART("h", "\u2665"),
    CLUB("c", "\u2663"),
    DIAMOND("d", "\u2666");

    override fun toString(): String {
        return symbol
    }

    companion object {
        fun fromText(symbol: String): CardSuit {
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