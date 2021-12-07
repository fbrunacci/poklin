package poklin.model.cards

import java.io.Serializable

class Card(val suit: CardSuit, val number: CardNumber) : Comparable<Card>, Serializable {

    companion object {
        fun fromText(text: String): Card {
            val number = text.substring(0..text.length-2)
            val suit = text.substring(text.length-1 until text.length)
            return Card(CardSuit.fromText(suit),CardNumber.fromText(number))
        }
    }

    override fun toString(): String {
        return suit.toString() + number.toString()
    }

    fun toText(): String {
        return number.toString() + suit.text
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