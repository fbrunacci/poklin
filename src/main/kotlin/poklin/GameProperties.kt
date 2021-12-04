package poklin

import java.util.*

abstract class GameProperties(
    val numberOfHands: Int,
    val bigBlind: Int,
    val smallBlind: Int
) {
    val players = LinkedList<Player>()
    protected fun addPlayer(player: Player) {
        players.add(player)
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