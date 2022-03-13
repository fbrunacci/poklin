package poklin

import com.google.inject.Guice
import com.google.inject.Inject
import org.junit.Before
import org.junit.Test
import poklin.controler.GameController
import poklin.helper.PlayerControllerBettingDecisionFifo
import poklin.injection.TexasModule
import poklin.injection.ConsoleLoggerModule
import poklin.injection.toModule
import poklin.model.bet.BettingDecision
import poklin.model.bet.BettingDecision.Companion.RAISE_CUSTOM

internal class PlayRound3WithPlayersTest {

    class Test3PlayersGameProperties : GameProperties(20f, 10f, dealerSeat = 1) {
        val player1Controller = PlayerControllerBettingDecisionFifo()
        val player2Controller = PlayerControllerBettingDecisionFifo()
        val player3Controller = PlayerControllerBettingDecisionFifo()

        val player1 = Player(1, 1000f, player1Controller)
        val player2 = Player(2, 1000f, player2Controller)
        val player3 = Player(3, 1000f, player3Controller)

        init {
            addPlayer(player1)
            addPlayer(player2)
            addPlayer(player3)
        }
    }

    private val testGameProperties = Test3PlayersGameProperties()

    @Inject
    lateinit var gameController: GameController

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val injector = Guice.createInjector(TexasModule(), ConsoleLoggerModule(), testGameProperties.toModule())
        injector.injectMembers(this)
    }

    @Test
    fun playRound() {
        val game = gameController.createGame()

        with(testGameProperties) {
            player1Controller.pushDecision(RAISE_CUSTOM(1000f))
            player2Controller.pushDecision(RAISE_CUSTOM(990f))
            player3Controller.pushDecision(BettingDecision.CALL)
        }
        gameController.play(game)

        with(testGameProperties) {
            println("=====================")
            println(player1)
            println(player2)
            println(player3)
        }
    }
}