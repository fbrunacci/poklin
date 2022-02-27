package poklin

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import poklin.compose.state.TableState
import poklin.controler.player.PlayerControllerNormal
import poklin.model.bet.BettingDecision.BettingAction.RAISE
import java.util.*

internal class BettingRoundTest {
    private val players: LinkedList<Player> = LinkedList<Player>()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        players.clear()
        players.add(Player(1, 100, PlayerControllerNormal()))
        players.add(Player(2, 100, PlayerControllerNormal()))
    }

    val gameProperties = GameProperties(20, 10, 1)
    val tableState = TableState(gameProperties)

    @Test
    fun testPlayerCanBetWhenRoundStart() {
        val game = Game(tableState, players, 10, 20, dealerSeat = 1)
        val bettingRound = BettingRound(game)
        Assert.assertTrue(bettingRound.playerCanBet())
    }

    @Test
    fun testPlayerCanBetWhenP2HasNoActionStart() {
        val game = Game(tableState, players, 10, 20, dealerSeat = 1)
        val bettingRound = BettingRound(game)
        bettingRound.placeBet(players.iterator().next(), RAISE, 10)
        Assert.assertTrue(bettingRound.playerCanBet())
    }
}