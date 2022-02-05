package poklin

import java.util.*

class Games(val players: LinkedList<Player>) {
    private val games: MutableList<Game> = ArrayList()

    init {

    }

    fun setNextDealer() {
        val formerDealer = players.removeAt(0)
        players.add(formerDealer)
    }

    fun addPlayer(player: Player) {
        players.add(player)
    }

    fun addGame(game: Game) {
        games.add(game)
    }

    fun gamesCount(): Int {
        return games.size
    }
}