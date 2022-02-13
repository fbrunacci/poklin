package poklin.model.cards

import org.junit.Assert.assertEquals
import org.junit.Test
import poklin.model.cards.CardNumber.ACE
import poklin.model.cards.CardNumber.TEN
import poklin.model.cards.CardSuit.*

internal class CardTest {

    @Test
    fun toText() {
        assertEquals("Ad", Card(DIAMOND, ACE).toText())
        assertEquals("As", Card(SPADE, ACE).toText())
        assertEquals("Ac", Card(CLUB, ACE).toText())
        assertEquals("Ah", Card(HEART, ACE).toText())
    }

    @Test
    fun fromText() {
        assertEquals(Card(DIAMOND, ACE), Card.fromText("Ad"))
        assertEquals(Card(DIAMOND, TEN), Card.fromText("10d"))
    }

    @Test
    fun fromCardsText() {
        assertEquals(
            listOf(Card(DIAMOND, ACE), Card(SPADE, ACE), Card(CLUB, ACE), Card(HEART, ACE)),
            Cards.fromText("Ad As Ac Ah")
        )
    }
}