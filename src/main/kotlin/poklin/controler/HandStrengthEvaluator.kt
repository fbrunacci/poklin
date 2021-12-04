package poklin.controler

import poklin.dependencyinjection.HandPowerRanker
import poklin.model.cards.Card
import poklin.model.cards.Deck
import javax.inject.Inject

class HandStrengthEvaluator @Inject constructor(private val handPowerRanker: HandPowerRanker) {
    fun evaluate(playerHoleCards: List<Card>?, sharedCards: List<Card>?, numberOfPlayers: Int): Double {
        if (sharedCards == null || sharedCards.isEmpty()) {
            return 0.0
        }
        var wins = 0
        var losses = 0
        var ties = 0
        val deck = Deck()
        val hole1 = playerHoleCards!![0]
        val hole2 = playerHoleCards[1]
        deck.removeCard(hole1)
        deck.removeCard(hole2)
        for (card in sharedCards) {
            deck.removeCard(card)
        }
        val couplesOfCards = deck.fromDeckToCouplesOfCard()
        val playerCards: MutableList<Card> = ArrayList()
        playerCards.addAll(playerHoleCards)
        playerCards.addAll(sharedCards)
        val playerRank = handPowerRanker.rank(playerCards)
        for (couple in couplesOfCards) {
            val opponentCards: MutableList<Card> = ArrayList()
            opponentCards.addAll(couple)
            opponentCards.addAll(sharedCards)
            val opponentRank = handPowerRanker.rank(opponentCards)
            val result = playerRank.compareTo(opponentRank)
            if (result > 0) {
                wins++
            } else if (result < 0) {
                losses++
            } else {
                ties++
            }
        }
        return calculateHandStrength(wins, ties, losses, numberOfPlayers)
    }

    private fun calculateHandStrength(wins: Int, ties: Int, losses: Int, numberOfPlayers: Int): Double {
        val num = wins + 0.5 * ties
        val den = (wins + losses + ties).toDouble()
        return Math.pow(num / den, numberOfPlayers.toDouble())
    }
}