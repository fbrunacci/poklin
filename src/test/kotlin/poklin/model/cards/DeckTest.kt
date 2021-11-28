package poklin.model.cards

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
        val clubAs = Card(CardSuit.fromString("c"), CardNumber.fromSymbol("A"))
        val diamondAs = Card(CardSuit.DIAMOND, CardNumber.ACE)
        deck.pushCardToTop(diamondAs)
        deck.pushCardToTop(clubAs)
        assertEquals(diamondAs, deck.removeTopCard())
        assertEquals(clubAs, deck.removeTopCard())
    }
}