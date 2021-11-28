package poklin.model.cards

import java.io.Serializable
import java.util.*

class DeckFifo : Deck(), Serializable {

    private val predefinedCards = LinkedList<Card>()

    override fun removeTopCard(): Card {
        return if (!predefinedCards.isEmpty()) {
            predefinedCards.removeFirst()
        } else super.removeTopCard()
    }

    fun pushCardToTop(card: Card) {
        removeCard(card)
        predefinedCards.add(card)
    }
}