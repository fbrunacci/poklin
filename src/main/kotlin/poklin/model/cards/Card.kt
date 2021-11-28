package poklin.model.cards

import java.io.Serializable

class Card(val suit: CardSuit, val number: CardNumber) : Comparable<Card>, Serializable {
    override fun toString(): String {
        return suit.toString() + number.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Card) {
            return false
        }
        return suit == other.suit && number == other.number
    }

    override fun compareTo(other: Card): Int {
        return number.power - other.number.power
    }
}