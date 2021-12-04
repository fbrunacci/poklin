package poklin.model.cards

class EquivalenceClassSuited(number1: CardNumber, number2: CardNumber) : EquivalenceClass(number1, number2) {
    override val type: String
        get() = "SUITED"

    override fun equivalence2cards(): List<Card> {
        return listOf(Card(CardSuit.SPADE, number1), Card(CardSuit.SPADE, number2))
    }

    override fun toString(): String {
        return ("Equivalence Class Suited (" + number1 + ","
                + number2 + ")")
    }
}