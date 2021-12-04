package poklin

import com.google.inject.Guice
import com.google.inject.Singleton
import dev.misfitlabs.kotlinguice4.KotlinModule
import org.junit.Before
import org.junit.Test
import poklin.PlayRoundTest.TestGamePropertiesModule.TestGameProperties
import poklin.controler.GameHandController
import poklin.dependencyinjection.TexasModule
import poklin.model.bet.BettingDecision
import poklin.model.bet.BettingDecision.Companion.RAISE_CUSTOM
import poklin.utils.ConsoleLogger
import poklin.utils.ILogger

class PlayRoundTest {

    class TestGamePropertiesModule : KotlinModule() {
        override fun configure() {
            bind<GameProperties>().to<TestGameProperties>().`in`<Singleton>()
            bind<ILogger>().to<ConsoleLogger>().`in`<Singleton>()
        }

        class TestGameProperties : GameProperties(15, 20, 10) {
            val player1Controller = PlayerControllerBettingDecisionFifo()
            val player2Controller = PlayerControllerBettingDecisionFifo()

            val player1 = Player(1, 1000, player1Controller)
            val player2 = Player(2, 1000, player2Controller)

            init {
                addPlayer(player1)
                addPlayer(player2)
            }
        }
    }

    lateinit var gameHandController: GameHandController
    lateinit var testGameProperties: TestGameProperties

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val injector = Guice.createInjector(TexasModule(), TestGamePropertiesModule())
        gameHandController = injector.getInstance(GameHandController::class.java)
        testGameProperties = injector.getInstance(GameProperties::class.java) as TestGameProperties
    }

    @Test
    fun playRound() {
        val gameHand = GameHand(testGameProperties.players, 10, 20)
        with(testGameProperties) {
            player2Controller.pushDecision(RAISE_CUSTOM(50))
            player1Controller.pushDecision(BettingDecision.CALL)
            player2Controller.pushDecision(BettingDecision.FOLD)
        }
        gameHandController.play(gameHand)

        with(testGameProperties) {
            println("=====================")
            println(player1)
            println(player2)
        }
    }

    @Test
    fun playRoundS1() {
        val gameHand = GameHand(testGameProperties.players, 10, 20)
        with(testGameProperties) {
            player2Controller.pushDecision(RAISE_CUSTOM(50))
            player1Controller.pushDecision(BettingDecision.CALL)
            gameHandController.playRound(gameHand)
            player2Controller.pushDecision(BettingDecision.FOLD)
            gameHandController.playRound(gameHand)
        }
    }

    @Test
    fun playRoundS2() {
        val gameHand = GameHand(testGameProperties.players, 10, 20)
        with(testGameProperties) {
            player2Controller.pushDecision(RAISE_CUSTOM(50))
            player1Controller.pushDecision(BettingDecision.CALL)
            gameHandController.playRound(gameHand)
            player2Controller.pushDecision(BettingDecision.CHECK)
            player1Controller.pushDecision(BettingDecision.FOLD)
            gameHandController.playRound(gameHand)
        }
    }

    @Test
    fun playRoundS3() {
        val gameHand = GameHand(testGameProperties.players, 10, 20)
        with(testGameProperties) {
            player2Controller.pushDecision(RAISE_CUSTOM(50))
            player1Controller.pushDecision(BettingDecision.CALL)
            gameHandController.playRound(gameHand)
            player2Controller.pushDecision(BettingDecision.CHECK)
            player1Controller.pushDecision(BettingDecision.CHECK)
            gameHandController.playRound(gameHand)
            player2Controller.pushDecision(BettingDecision.CHECK)
            player1Controller.pushDecision(BettingDecision.CHECK)
            gameHandController.playRound(gameHand)
            player2Controller.pushDecision(BettingDecision.CHECK)
            player1Controller.pushDecision(BettingDecision.CHECK)
            gameHandController.playRound(gameHand)
        }
    }

//    @Test
//    fun playRoundCHECK() { // pas ok car 2eme check interdit
//        val gameHand = GameHand(testGameProperties.players, 10, 20)
//        with(testGameProperties) {
//            player2Controller.pushDecision(BettingDecision.CHECK)
//            player1Controller.pushDecision(BettingDecision.CHECK)
//            gameHandController.playRound(gameHand)
//        }
//    }

    @Test
    fun playGameS3() {
        val gameHand = GameHand(testGameProperties.players, 10, 20)
        with(testGameProperties) {
            // preflop
            player2Controller.pushDecision(RAISE_CUSTOM(50))
            player1Controller.pushDecision(BettingDecision.CALL)

            // post flop
            player2Controller.pushDecision(BettingDecision.CHECK)
            player1Controller.pushDecision(BettingDecision.CHECK)

            // post turn
            player2Controller.pushDecision(BettingDecision.CHECK)
            player1Controller.pushDecision(BettingDecision.CHECK)

            // post river
            player2Controller.pushDecision(BettingDecision.CHECK)
            player1Controller.pushDecision(BettingDecision.CHECK)
            gameHandController.play(gameHand)
        }
    }

    @Test
    fun playGameS4() {
        val gameHand = GameHand(testGameProperties.players, 10, 20)
        with(testGameProperties) {
            // preflop
            player2Controller.pushDecision(RAISE_CUSTOM(50))
            player1Controller.pushDecision(BettingDecision.CALL)

            // post flop
            player2Controller.pushDecision(BettingDecision.CHECK)
            player1Controller.pushDecision(BettingDecision.CHECK)

            // post turn
            player2Controller.pushDecision(BettingDecision.CHECK)
            player1Controller.pushDecision(BettingDecision.CHECK)

            // post river
            player2Controller.pushDecision(BettingDecision.FOLD)
            gameHandController.play(gameHand)
        }
    }

    @Test
    fun playGameSplitPot() {
        val gameHand = GameHand(testGameProperties.players, 10, 20)
        with(testGameProperties) {
            // preflop
            player2Controller.pushDecision(RAISE_CUSTOM(50))
            player1Controller.pushDecision(BettingDecision.CALL)

            // post flop
            player2Controller.pushDecision(BettingDecision.CHECK)
            player1Controller.pushDecision(BettingDecision.CHECK)

            // post turn
            player2Controller.pushDecision(BettingDecision.CHECK)
            player1Controller.pushDecision(BettingDecision.CHECK)

            // post river
            player2Controller.pushDecision(BettingDecision.FOLD)
            gameHandController.play(gameHand)
        }
    }
}