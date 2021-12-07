package poklin.model.cards

object Cards {

    fun fromText(text: String): List<Card> {
        return text.split(" ").map(Card::fromText).toList()
    }

}