package poklin.dependencyinjection.preflopsim

import poklin.GameHand
import poklin.Player
import poklin.model.cards.Deck
import poklin.model.cards.EquivalenceClass
import java.util.*

class GameHandPreFlopRoll(
    players: LinkedList<Player>,
    smallBlind: Int,
    bigBlind: Int,
    private val equivalenceClass: EquivalenceClass?
) : GameHand(players, smallBlind, bigBlind, Deck()) {
    /**
     * Deals the hole cards. The prospective of the simulation is player0's one,
     * so players0's hole cards are the same of equivalence cards, while the
     * other players receive random cards form the top of the deck.
     */
    override fun dealHoleCards() {
        val deck = deck
        val holeCards = equivalenceClass!!.equivalence2cards()
        var hole1 = holeCards[0]
        var hole2 = holeCards[1]
        for (player in table.players) {
            if (player!!.seat == 1) {
                player.setHoleCards(hole1, hole2)
                deck.removeCard(hole1)
                deck.removeCard(hole2)
            }
        }

        // other players card are random
        for (player in table.players) {
            if (player!!.seat != 1) {
                hole1 = deck.removeTopCard()
                hole2 = deck.removeTopCard()
                player.setHoleCards(hole1, hole2)
            }
        }
    }
}