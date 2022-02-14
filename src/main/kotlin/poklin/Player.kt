package poklin

import poklin.compose.state.PlayerState
import poklin.controler.player.PlayerController
import poklin.model.bet.BettingDecision
import poklin.model.cards.Card
import java.util.*

class Player(val seat: Int, money: Int, val playerController: PlayerController) {

    val playerState = PlayerState("Player${seat}", seat)

    init {
        playerState.money = money
    }

    var money: Int = money
        get() = field
        set(value) {
            field = value
            playerState.money = field
        }

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
        stringBuilder.append("$seat ($money$) ".padEnd(11))
        if (holeCards != null) {
            stringBuilder.append(holeCards.toString().padEnd(10))
        }
        return stringBuilder.toString()
    }

    fun info(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("Player#")
        stringBuilder.append("$seat ($money$) ".padEnd(11))
        return stringBuilder.toString()
    }

    fun decide(game: Game): BettingDecision {
        return playerController.doDecide(this, game)
    }

    fun removeMoney(amount: Int) {
        money -= amount
        playerState?.money = money
    }

    fun addMoney(amount: Int) {
        money += amount
        playerState?.money = money
    }

    fun setHoleCards(hole1: Card, hole2: Card) {
        holeCards = Arrays.asList(hole1, hole2)
        playerState?.card1 = hole1.toText()
        playerState?.card2 = hole2.toText()
    }
}