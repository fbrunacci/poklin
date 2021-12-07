package poklin

import java.util.*

class Game(val players: LinkedList<Player>) {
    private val gameHands: MutableList<GameHand> = ArrayList()

    fun setNextDealer() {
        val formerDealer = players.removeAt(0)
        players.add(formerDealer)
    }

    fun addPlayer(player: Player) {
        players.add(player)
    }

    fun addGameHand(gameHand: GameHand) {
        gameHands.add(gameHand)
    }

    fun gameHandsCount(): Int {
        return gameHands.size
    }
}