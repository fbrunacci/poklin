package poklin

import java.util.*

class Table(val maxSeats: Int, var dealerSeat: Int, players: List<Player>) {

    var currentPlayerSeat = -1
    var playersMap: MutableMap<Int, Player> = HashMap()
    private lateinit var activePlayers: MutableList<Player>
    private lateinit var inGamePlayers: MutableList<Player>

    init {
        initActivePlayers(players)
        initInGamePlayers(players)
        for (player in players) {
            playersMap[player.seat] = player
        }
    }

    constructor(maxSeats: Int, players: LinkedList<Player>) : this(maxSeats, 1, players) { // TODO remove this
    }

    private fun initActivePlayers(players: Collection<Player>) {
        activePlayers = LinkedList()
        for (player in players) {
            if (player.money > 0) activePlayers.add(player)
        }
    }

    private fun initInGamePlayers(players: Collection<Player>) {
        inGamePlayers = LinkedList()
        for (player in players) {
            if (player.money > 0) inGamePlayers.add(player)
        }
    }

    val players: Collection<Player?>
        get() = playersMap.values

    fun addPlayer(p: Player) {
        // TODO check money > 0
        playersMap[p.seat] = p
        activePlayers.add(p)
        inGamePlayers.add(p)
    }

    fun setPlayerOut(p: Player?) {
        inGamePlayers.remove(p)
        activePlayers.remove(p)
    }

    fun setCurrentPlayerOut() {
        setPlayerOut(currentPlayer())
    }

    fun nextPlayer(): Player? {
        if (currentPlayerSeat < 0) {
            currentPlayerSeat = dealerSeat
        }
        // inc
        currentPlayerSeat = (currentPlayerSeat + 1) % maxSeats
        while (!inGamePlayers.contains(playersMap[currentPlayerSeat]) || playersMap[currentPlayerSeat]!!.money == 0) {
            currentPlayerSeat = (currentPlayerSeat + 1) % maxSeats
        }
        return playersMap[currentPlayerSeat]
    }

    fun currentPlayer(): Player? {
        return playersMap[currentPlayerSeat]
    }

    /**
     * return still active players to decide some action (bet / fold ...)
     * @return
     */
    fun getInGamePlayers(): List<Player> {
        return inGamePlayers
    }

    /**
     * @return still active player for the pot (in game + allin player)
     */
    fun getActivePlayers(): List<Player> {
        return activePlayers
    }

    /**
     * dealer++
     * currentPlayer = afterDealer
     * all player with money are active / in game
     */
    fun newRound() {
        currentPlayerSeat = -1
    }
}