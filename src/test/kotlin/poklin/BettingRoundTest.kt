package poklin

import com.google.inject.Guice
import com.google.inject.Injector
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import poklin.dependencyinjection.HandPowerRanker
import poklin.dependencyinjection.TexasModule
import poklin.dependencyinjection.preflopsim.PlayerControllerPreFlopRoll
import poklin.model.bet.BettingDecision.BettingAction.RAISE
import java.lang.Exception
import java.util.*

class BettingRoundTest {
//    private var gameHandController: GameHandController? = null
//    private var playerController: PlayerController? = null
    val players: LinkedList<Player> = LinkedList<Player>()

    @Before
    @Throws(Exception::class)
    fun setUp() {
//        val injector: Injector = Guice.createInjector(TexasModule())
//        gameHandController = injector.getInstance(GameHandController::class.java)
//        playerController = injector
//            .getInstance(PlayerControllerPreFlopRoll::class.java)
        players.clear()
        val preFlopRollController = PlayerControllerPreFlopRoll(HandPowerRanker())
        players.add(Player(1, 100, preFlopRollController))
        players.add(Player(2, 100, preFlopRollController))
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