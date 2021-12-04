package poklin

import poklin.controler.PlayerController
import poklin.model.bet.BettingDecision
import poklin.model.cards.Card
import java.util.*

class Player(
    val seat: Int, var money: Int,
    val playerController: PlayerController
) {
    var holeCards: List<Card>? = null
        private set

    override fun equals(o: Any?): Boolean {
        if (o !is Player) {
            return false
        }
        return seat == o.seat
    }

    override fun hashCode(): Int {
        return seat
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("Player#")
        stringBuilder.append("$seat ($money$) ".padEnd(10))
        if (holeCards != null) {
            stringBuilder.append(holeCards.toString())
        }
        return stringBuilder.toString()
    }

    fun decide(gameHand: GameHand): BettingDecision {
        return playerController.doDecide(this, gameHand)
    }

    fun removeMoney(amount: Int) {
        money -= amount
    }

    fun addMoney(amount: Int) {
        money += amount
    }

    fun setHoleCards(hole1: Card, hole2: Card) {
        holeCards = Arrays.asList(hole1, hole2)
    }
}