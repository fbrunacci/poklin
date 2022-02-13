package poklin

import java.util.*

open class GameProperties(
    val bigBlind: Int,
    val smallBlind: Int,
    var dealerSeat: Int
) {
    val players = LinkedList<Player>()

    fun addPlayer(player: Player) {
        players.add(player)
    }

    fun nextDealer() {
        // TODO test si players avec money > 1
        val maxSeats = players.size + 1
        dealerSeat = (dealerSeat + 1) % maxSeats
        var nextDealer = players.firstOrNull{ it.seat == dealerSeat }
        while ( nextDealer == null ||  (nextDealer != null && nextDealer.money <= 0)) {
            dealerSeat = (dealerSeat + 1) % maxSeats
            nextDealer = players.firstOrNull{ it.seat == dealerSeat }
        }
    }

    val nbPlayersWithMoney: Int
        get() {
            var nbPlayerWithMoney = 0
            val it: Iterator<Player> = players.iterator()
            while (it.hasNext()) {
                if (it.next().money > 0) {
                    nbPlayerWithMoney++
                }
            }
            return nbPlayerWithMoney
        }
}