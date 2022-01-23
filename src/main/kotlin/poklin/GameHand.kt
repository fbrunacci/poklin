package poklin

import poklin.compose.state.TableState
import poklin.model.bet.BettingDecision
import poklin.model.bet.BettingRoundName
import poklin.model.bet.BettingRoundName.Companion.fromRoundNumber
import poklin.model.cards.Card
import poklin.model.cards.Deck
import poklin.model.cards.IDeck
import poklin.opponentmodeling.ContextAction
import poklin.opponentmodeling.ContextInformation
import java.util.*

open class GameHand(val players: LinkedList<Player>, val smallBlind: Int, val bigBlind: Int, val deck: IDeck = Deck()) {

    val table = Table(6, players)
    val sharedCards: MutableList<Card> = ArrayList()
    private val bettingRounds: MutableList<BettingRound> = ArrayList()
    private val pots: MutableList<Pot> = ArrayList()

    fun nextRound(): BettingRound {
        table.newRound()
        val bettingRound = BettingRound(this)
        bettingRounds.add(bettingRound)
        if (bettingRoundName == BettingRoundName.PRE_FLOP) {
            dealHoleCards()
        } else if (bettingRoundName == BettingRoundName.POST_FLOP) {
            dealFlopCards()
        } else {
            dealSharedCard()
        }
        return bettingRound
    }

    val totalBets: Int
        get() {
            var totalBets = 0
            for (bettingRound in bettingRounds) {
                totalBets += bettingRound.totalBets
            }
            return totalBets
        }
    val bettingRoundName: BettingRoundName
        get() = fromRoundNumber(bettingRounds.size)

    val currentBettingRound: BettingRound
        get() = bettingRounds[bettingRounds.size - 1]

    fun getBettingRounds(): List<BettingRound> {
        return bettingRounds
    }

    protected open fun dealHoleCards() {
        for (player in table.getInGamePlayers()) {
            val hole1 = deck.removeTopCard()
            val hole2 = deck.removeTopCard()
            player.setHoleCards(hole1, hole2)
        }
    }

    private fun dealFlopCards() {
        val sharedCard1 = deck.removeTopCard()
        val sharedCard2 = deck.removeTopCard()
        val sharedCard3 = deck.removeTopCard()
        sharedCards.add(sharedCard1)
        sharedCards.add(sharedCard2)
        sharedCards.add(sharedCard3)
        TableState.sharedCard1 = sharedCard1.toText()
        TableState.sharedCard2 = sharedCard2.toText()
        TableState.sharedCard3 = sharedCard3.toText()
    }

    private fun  dealSharedCard() {
        val sharedCard = deck.removeTopCard()
        sharedCards.add(sharedCard)
        if (bettingRoundName == BettingRoundName.POST_TURN) {
            TableState.sharedCard4 = sharedCard.toText()
        } else {
            TableState.sharedCard5 = sharedCard.toText()
        }
    }

    fun applyDecision(
        player: Player?, bettingDecision: BettingDecision, gameProperties: GameProperties?,
        handStrength: Double
    ) {
        val currentBettingRound = currentBettingRound
        val potOdds = calculatePotOdds(player)
        val contextAction = ContextAction(
            player!!, bettingDecision, bettingRoundName,
            currentBettingRound.numberOfRaises,
            table.getInGamePlayers().size, potOdds
        )
        val contextInformation = ContextInformation(contextAction, handStrength)
        currentBettingRound.applyDecision(contextInformation, gameProperties!!)
        if (bettingDecision == BettingDecision.FOLD) {
            table.setCurrentPlayerOut()
        }
    }

    fun calculatePotOdds(player: Player?): Double {
        val currentBettingRound = currentBettingRound
        val amountNeededToCall = currentBettingRound.highestBet - currentBettingRound.getBetForPlayer(player!!)
        return amountNeededToCall.toDouble() / (amountNeededToCall + totalBets)
    }

    fun contributePot(amount: Int, player: Player) {
        TableState.pot += amount
        TableState.playerStateAtSeat(player.seat).moneyPutInPot += amount

        var amount = amount
        for (pot in pots) {
            if (!pot.hasContributer(player)) {
                val potBet = pot.bet
                if (amount >= potBet) {
                    // Regular call, bet or raise.
                    pot.addContributer(player)
                    amount -= pot.bet
                } else {
                    // Partial call (all-in); redistribute pots.
                    pots.add(pot.split(player, amount))
                    amount = 0
                }
            }
            if (amount <= 0) {
                break
            }
        }
        if (amount > 0) {
            val pot = Pot(amount)
            pot.addContributer(player!!)
            pots.add(pot)
        }
    }

    fun getPots(): List<Pot> {
        return pots
    }

    val totalPot: Int
        get() {
            var totalPot = 0
            for (pot in pots) {
                totalPot += pot.value
            }
            return totalPot
        }

}