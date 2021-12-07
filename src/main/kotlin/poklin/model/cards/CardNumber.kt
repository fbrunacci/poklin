package poklin.model.cards

import java.io.Serializable

enum class CardNumber(val symbol: String, val power: Int) : Serializable {
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10),
    JACK("J", 11),
    QUEEN("Q", 12),
    KING("K", 13),
    ACE("A", 14);

    override fun toString(): String {
        return symbol
    }

    companion object {
        fun fromText(symbol: String): CardNumber {
            return CardNumber.values().first { it.symbol == symbol }
        }
    }
}