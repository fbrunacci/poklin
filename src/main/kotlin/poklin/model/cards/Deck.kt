package poklin.model.cards

import java.io.Serializable
import java.util.*

open class Deck : IDeck(), Serializable {

    private val cards: MutableList<Card> = ArrayList()

    init {
        for (suit in CardSuit.values()) {
            for (number in CardNumber.values()) {
                val card = Card(suit, number)
                cards.add(card)
            }
        }
        Collections.shuffle(cards)
    }

    fun getCards(): List<Card> {
        return cards
    }

    override fun removeTopCard(): Card {
        return cards.removeAt(0)
    }

    override fun removeCard(card: Card): Boolean {
        return cards.remove(card)
    }

    fun fromDeckToCouplesOfCard(): List<List<Card>> {
        val couplesOfCard: MutableList<List<Card>> = ArrayList()
        var i: Int
        var j: Int
        i = 0
        while (i < cards.size) {
            j = i + 1
            while (j < cards.size) {
                val tmpCards: MutableList<Card> = ArrayList()
                tmpCards.add(cards[i])
                tmpCards.add(cards[j])
                couplesOfCard.add(tmpCards)
                j++
            }
            i++
        }
        return couplesOfCard
    }
}