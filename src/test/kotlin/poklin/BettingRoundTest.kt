package poklin

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import poklin.controler.player.PlayerControllerNormal
import poklin.model.bet.BettingDecision.BettingAction.RAISE
import java.lang.Exception
import java.util.*

class BettingRoundTest {
    val players: LinkedList<Player> = LinkedList<Player>()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        players.clear()
        players.add(Player(1, 100, PlayerControllerNormal()))
        players.add(Player(2, 100, PlayerControllerNormal()))
    }

    @Test
    fun testPlayerCanBetWhenRoundStart() {
        val gameHand = GameHand(players, 10, 20)
        val bettingRound = BettingRound(gameHand)
        Assert.assertTrue(bettingRound.playerCanBet())
    }

    @Test
    fun testPlayerCanBetWhenP2HasNoActionStart() {
        val gameHand = GameHand(players, 10, 20)
        val bettingRound = BettingRound(gameHand)
        bettingRound.placeBet(players.iterator().next(), RAISE, 10)
        Assert.assertTrue(bettingRound.playerCanBet())
    }
}