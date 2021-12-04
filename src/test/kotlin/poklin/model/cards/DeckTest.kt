package poklin.model.cards

import poklin.model.cards.CardNumber.ACE
import poklin.model.cards.CardSuit.DIAMOND
import kotlin.test.Test
import kotlin.test.assertEquals

internal class DeckTest {

    @Test
    fun testDeck() {
        val deck = Deck()
        val cards = deck.getCards()
        assertEquals(52, cards.size)
        println(deck.removeTopCard())
    }

    @Test
    fun testCloneDeck() {
        val deck1: IDeck = Deck()
        val deck2 = deck1.clone()
        assertEquals(deck1.removeTopCard(), deck2.removeTopCard())
    }

    @Test
    fun testDeckFifo() {
        val deck = DeckFifo()
        val clubAce = Card(CardSuit.fromString("c"), CardNumber.fromSymbol("A"))
        val diamondAce = Card(DIAMOND, ACE)
        deck.pushCardToTop(diamondAce)
        deck.pushCardToTop(clubAce)
        assertEquals(diamondAce, deck.removeTopCard())
        assertEquals(clubAce, deck.removeTopCard())
    }
}