package poklin

import com.google.inject.Guice
import com.google.inject.Singleton
import dev.misfitlabs.kotlinguice4.KotlinModule
import org.junit.Before
import org.junit.Test
import poklin.compose.state.TableState
import poklin.controler.GameController
import poklin.dependencyinjection.TexasModule
import poklin.model.bet.BettingDecision
import poklin.model.bet.BettingDecision.Companion.RAISE_CUSTOM
import poklin.utils.ConsoleLogger
import poklin.utils.ILogger

internal class PlayRound3PlayersTest {

    class TestGamePropertiesModule : KotlinModule() {
        override fun configure() {
            bind<GameProperties>().to<TestGameProperties>().`in`<Singleton>()
            bind<ILogger>().to<ConsoleLogger>().`in`<Singleton>()
        }

        class TestGameProperties : GameProperties(20, 10, dealerSeat = 1) {
            val player1Controller = PlayerControllerBettingDecisionFifo()
            val player2Controller = PlayerControllerBettingDecisionFifo()
            val player3Controller = PlayerControllerBettingDecisionFifo()

            val player1 = Player(1, 1000, player1Controller)
            val player2 = Player(2, 1000, player2Controller)
            val player3 = Player(3, 1000, player3Controller)

            init {
                addPlayer(player1)
                addPlayer(player2)
                addPlayer(player3)
            }
        }
    }

    lateinit var gameController: GameController
    lateinit var testGameProperties: TestGamePropertiesModule.TestGameProperties

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val injector = Guice.createInjector(TexasModule(), TestGamePropertiesModule())
        gameController = injector.getInstance(GameController::class.java)
        testGameProperties = injector.getInstance(GameProperties::class.java) as TestGamePropertiesModule.TestGameProperties
    }

    @Test
    fun playRound() {
        TableState.newGame(testGameProperties)
        val game = gameController.createGame()

        with(testGameProperties) {
            player1Controller.pushDecision(RAISE_CUSTOM(1000))
            player2Controller.pushDecision(RAISE_CUSTOM(990))
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