package poklin.model.cards

class EquivalenceClassUnsuited(number1: CardNumber, number2: CardNumber) : EquivalenceClass(number1, number2) {
    override val type: String
        get() = "UNSUITED"

    override fun equivalence2cards(): List<Card> {
        return listOf(Card(CardSuit.HEART, number1), Card(CardSuit.SPADE, number1))
    }

    override fun toString(): String {
        return ("Equivalence Class Unsuited (" + number1 + ","
                + number2 + ")")
    }
}