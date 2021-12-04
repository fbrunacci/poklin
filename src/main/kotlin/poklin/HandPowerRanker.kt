package poklin

import poklin.model.HandPower
import poklin.model.HandPowerType
import poklin.model.cards.Card
import poklin.model.cards.CardNumber
import poklin.model.cards.CardSuit
import poklin.utils.MapList
import java.util.*

object HandPowerRanker {
    private val cardNumberComparator: Comparator<CardNumber?> =
        Comparator { cardNumber1, cardNumber2 -> cardNumber1!!.power - cardNumber2!!.power }

    fun rank(cards: List<Card>?): HandPower {
        val numberGroup = getNumberGroup(cards)
        val suitGroup = getSuitGroup(cards)
        val cardsSortedByNumber = getCardsSortedByNumber(cards)
        val straightFlushNumber = getStraightFlushNumber(suitGroup)

        // Straight flush
        if (straightFlushNumber != null) {
            return HandPower(
                HandPowerType.STRAIGHT_FLUSH,
                Arrays.asList(straightFlushNumber)
            )
        }
        val cardNumberForFour = getCardNumberForCount(4, numberGroup)
        // Four of a kind
        if (cardNumberForFour != null) {
            return HandPower(
                HandPowerType.FOUR_OF_A_KIND,
                calculateSameKindTie(
                    4, cardNumberForFour,
                    cardsSortedByNumber
                )
            )
        }
        val fullHouseCardNumbers = getFullHouse(numberGroup)
        // Full house
        if (fullHouseCardNumbers.size == 2) {
            return HandPower(HandPowerType.FULL_HOUSE, fullHouseCardNumbers)
        }

        // Flush
        val flushSuit = getFlush(suitGroup)
        if (flushSuit != null) {
            return HandPower(
                HandPowerType.FLUSH, calculateFlushTie(
                    flushSuit, suitGroup
                )
            )
        }

        // Straight
        val straightNumber = getStraight(numberGroup)
        if (straightNumber != null) {
            return HandPower(
                HandPowerType.STRAIGHT,
                Arrays.asList(straightNumber)
            )
        }

        // Three of a kind
        val cardNumberForThree = getCardNumberForCount(3, numberGroup)
        if (cardNumberForThree != null) {
            return HandPower(
                HandPowerType.THREE_OF_A_KIND,
                calculateSameKindTie(
                    3, cardNumberForThree,
                    cardsSortedByNumber
                )
            )
        }

        // Pair(s)
        val cardNumberForTwo = getCardNumberForCount(2, numberGroup)
        if (cardNumberForTwo != null) {
            val pairsCardNumber = getPairs(numberGroup)
            // Two pair
            return if (pairsCardNumber.size >= 2) {
                HandPower(
                    HandPowerType.TWO_PAIR,
                    calculateTwoPairsTie(
                        pairsCardNumber,
                        cardsSortedByNumber
                    )
                )
            } else {
                HandPower(
                    HandPowerType.ONE_PAIR,
                    calculateSameKindTie(
                        2, cardNumberForTwo,
                        cardsSortedByNumber
                    )
                )
            }
        }

        // High Card
        return HandPower(
            HandPowerType.HIGH_CARD,
            bestCardsNumberInList(cardsSortedByNumber)
        )
    }

    private fun getFullHouse(numberGroup: MapList<CardNumber, Card>): List<CardNumber?> {
        val fullHouseCardNumbers: MutableList<CardNumber?> = ArrayList()
        val cardNumbers: List<CardNumber?> = ArrayList(
            numberGroup.keySet()
        )
        Collections.sort(cardNumbers, cardNumberComparator)
        Collections.reverse(cardNumbers)

        // Find the best cards for the triple
        for (cardNumber in cardNumbers) {
            if (numberGroup[cardNumber!!].size >= 3) {
                fullHouseCardNumbers.add(cardNumber)
                break
            }
        }

        // Find the best card for the pair
        if (fullHouseCardNumbers.size > 0) {
            for (cardNumber in cardNumbers) {
                if (numberGroup[cardNumber!!].size >= 2
                    && cardNumber != fullHouseCardNumbers[0]
                ) {
                    fullHouseCardNumbers.add(cardNumber)
                    break
                }
            }
        }
        return fullHouseCardNumbers
    }

    private fun calculateTwoPairsTie(
        pairsCardNumber: List<CardNumber?>, cardsSortedByNumber: List<Card?>
    ): List<CardNumber?>? {
        Collections.sort(pairsCardNumber, cardNumberComparator)
        Collections.reverse(pairsCardNumber)
        val tieBreakingInformation: MutableList<CardNumber?> = ArrayList(
            pairsCardNumber
        )
        for (i in cardsSortedByNumber.indices.reversed()) {
            val cardNumber = cardsSortedByNumber[i]!!.number
            if (!pairsCardNumber.contains(cardNumber)) {
                tieBreakingInformation.add(cardNumber)
                return tieBreakingInformation
            }
        }
        return null
    }

    private fun getPairs(numberGroup: MapList<CardNumber, Card>): List<CardNumber?> {
        val pairsCardNumber: MutableList<CardNumber?> = ArrayList()
        for (cards in numberGroup) {
            if (cards.size == 2) {
                pairsCardNumber.add(cards[0].number)
            }
        }
        Collections.sort(pairsCardNumber, cardNumberComparator)
        Collections.reverse(pairsCardNumber)
        if (pairsCardNumber.size > 2) {
            pairsCardNumber.removeAt(pairsCardNumber.size - 1)
        }
        return pairsCardNumber
    }

    private fun calculateFlushTie(
        flushSuit: CardSuit,
        suitGroup: MapList<CardSuit, Card>
    ): List<CardNumber?> {
        val cards = suitGroup[flushSuit]
        return bestCardsNumberInList(cards)
    }

    private fun bestCardsNumberInList(cards: List<Card?>?): List<CardNumber?> {
        val cardNumbers = cardsToCardNumber(cards)
        Collections.sort(cardNumbers, cardNumberComparator)
        Collections.reverse(cardNumbers)
        return cardNumbers.subList(0, 5)
    }

    private fun getCardsSortedByNumber(cards: List<Card>?): List<Card?> {
        val cardsSortedByNumber: List<Card> = ArrayList(cards)
        Collections.sort(cardsSortedByNumber)
        return cardsSortedByNumber
    }

    private fun calculateSameKindTie(
        sameKindCount: Int,
        sameKindCardNumber: CardNumber, cardsSortedByNumber: List<Card?>
    ): List<CardNumber?> {
        val tieBreakingInformation: MutableList<CardNumber?> = ArrayList()
        tieBreakingInformation.add(sameKindCardNumber)
        var left = 5 - sameKindCount
        for (i in cardsSortedByNumber.indices.reversed()) {
            val card = cardsSortedByNumber[i]
            if (card!!.number != sameKindCardNumber && left > 0) {
                tieBreakingInformation.add(card.number)
                left--
            }
        }
        return tieBreakingInformation
    }

    private fun getCardNumberForCount(
        count: Int,
        numberGroup: MapList<CardNumber, Card>
    ): CardNumber? {
        for (entry in numberGroup.entrySet()) {
            if (entry.value.size == count) {
                return entry.key
            }
        }
        return null
    }

    private fun getStraight(numberGroup: MapList<CardNumber, Card>): CardNumber? {
        val cardNumbers: List<CardNumber?> = ArrayList(
            numberGroup.keySet()
        )
        return getStraightNumber(cardNumbers)
    }

    private fun getStraightFlushNumber(suitGroup: MapList<CardSuit, Card>): CardNumber? {
        val flushSuit = getFlush(suitGroup) ?: return null
        val cards = suitGroup[flushSuit]
        val cardNumbers = cardsToCardNumber(cards)
        return getStraightNumber(cardNumbers)
    }

    private fun cardsToCardNumber(cards: List<Card?>?): List<CardNumber?> {
        val cardNumbers: MutableList<CardNumber?> = ArrayList()
        for (card in cards!!) {
            cardNumbers.add(card!!.number)
        }
        return cardNumbers
    }

    private fun getStraightNumber(cardNumbers: List<CardNumber?>): CardNumber? {
        var straightNumber: CardNumber? = null
        var straightCount = 1
        var prevPower = 0
        Collections.sort(cardNumbers, cardNumberComparator)
        for (cardNumber in cardNumbers) {
            if (cardNumber!!.power == prevPower + 1) {
                straightCount++
                if (straightCount >= 5) {
                    straightNumber = cardNumber
                }
            } else {
                straightCount = 1
            }
            prevPower = cardNumber.power
        }
        return straightNumber
    }

    private fun getFlush(suitGroup: MapList<CardSuit, Card>): CardSuit? {
        for (cards in suitGroup) {
            if (cards.size >= 5) {
                return cards[0].suit
            }
        }
        return null
    }

    private fun getNumberGroup(cards: List<Card>?): MapList<CardNumber, Card> {
        val numberGroup = MapList<CardNumber, Card>()
        for (card in cards!!) {
            numberGroup.add(card.number, card)
        }
        return numberGroup
    }

    private fun getSuitGroup(cards: List<Card>?): MapList<CardSuit, Card> {
        val suitGroup = MapList<CardSuit, Card>()
        for (card in cards!!) {
            suitGroup.add(card.suit, card)
        }
        return suitGroup
    }
}