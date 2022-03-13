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
        players.add(Player(1, 100f, PlayerControllerNormal()))
        players.add(Player(2, 100f, PlayerControllerNormal()))
    }

    val gameProperties = GameProperties(20f, 10f, 1)
    val tableState = TableState()

    @Test
    fun testPlayerCanBetWhenRoundStart() {
        val game = Game(tableState, players, 10f, 20f, dealerSeat = 1)
        val bettingRound = BettingRound(game)
        Assert.assertTrue(bettingRound.playerCanBet())
    }

    @Test
    fun testPlayerCanBetWhenP2HasNoActionStart() {
        val game = Game(tableState, players, 10f, 20f, dealerSeat = 1)
        val bettingRound = BettingRound(game)
        bettingRound.placeBet(players.iterator().next(), RAISE, 10f)
        Assert.assertTrue(bettingRound.playerCanBet())
    }
}