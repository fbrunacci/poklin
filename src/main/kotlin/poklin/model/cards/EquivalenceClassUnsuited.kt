package poklin.model.cards

class EquivalenceClassUnsuited(number1: CardNumber, number2: CardNumber) : EquivalenceClass(number1, number2) {
    override fun equivalence2cards(): List<Card> {
        val cards: MutableList<Card> = ArrayList()
        val card1: Card
        val card2: Card
        card1 = Card(CardSuit.HEART, number1)
        card2 = Card(CardSuit.SPADE, number1)
        cards.add(card1)
        cards.add(card2)
        return cards
    }

    override val type: String
        get() = "UNSUITED"

    override fun toString(): String {
        return ("Equivalence Class Unsuited (" + number1 + ","
                + number2 + ")")
    }
}