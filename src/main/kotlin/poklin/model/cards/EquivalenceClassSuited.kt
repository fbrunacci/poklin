package poklin.model.cards

class EquivalenceClassSuited(number1: CardNumber, number2: CardNumber) : EquivalenceClass(number1, number2) {
    override val type: String
        get() = "SUITED"

    override fun equivalence2cards(): List<Card> {
        val cards: MutableList<Card> = ArrayList()
        val card1: Card
        val card2: Card
        card1 = Card(CardSuit.SPADE, number1)
        card2 = Card(CardSuit.SPADE, number2)
        cards.add(card1)
        cards.add(card2)
        return cards
    }

    override fun toString(): String {
        return ("Equivalence Class Suited (" + number1 + ","
                + number2 + ")")
    }
}