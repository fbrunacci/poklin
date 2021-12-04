package poklin

import com.google.inject.Guice
import com.google.inject.Singleton
import dev.misfitlabs.kotlinguice4.KotlinModule
import org.junit.Before
import org.junit.Test
import poklin.controler.GameHandController
import poklin.dependencyinjection.TexasModule
import poklin.model.bet.BettingDecision
import poklin.model.bet.BettingDecision.Companion.RAISE_CUSTOM
import poklin.persistence.PersistenceModule
import poklin.utils.ConsoleLogger
import poklin.utils.ILogger
import java.util.*

class PlayRoundTest {

    class TestGamePropertiesModule : KotlinModule() {
        override fun configure() {
            bind<GameProperties>().to<TestGameProperties>().`in`<Singleton>()
            bind<ILogger>().to<ConsoleLogger>().`in`<Singleton>()
        }
        class TestGameProperties : GameProperties(15, 1000, 20, 10) {
            val player1Controller = PlayerControllerBettingDecisionFifo()
            val player2Controller = PlayerControllerBettingDecisionFifo()

            init {
                addPlayer(Player(1, 100, player1Controller))
                addPlayer(Player(2, 100, player2Controller))
            }
        }
    }

    lateinit var gameHandController: GameHandController
    lateinit var testGameProperties: TestGamePropertiesModule.TestGameProperties

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val injector = Guice.createInjector(TexasModule(), TestGamePropertiesModule())
        gameHandController = injector.getInstance(GameHandController::class.java)
        testGameProperties = injector.getInstance(GameProperties::class.java) as TestGamePropertiesModule.TestGameProperties
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
        val players = LinkedList<Player>()
        val player1Controller = PlayerControllerBettingDecisionFifo()
        val player2Controller = PlayerControllerBettingDecisionFifo()
        players.add(Player(1, 100, player1Controller))
        players.add(Player(2, 100, player2Controller))
        val gameHand = GameHand(players, 10, 20)

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

    @Test
    fun playGameS4() {
        val players = LinkedList<Player>()
        val player1Controller = PlayerControllerBettingDecisionFifo()
        val player2Controller = PlayerControllerBettingDecisionFifo()
        players.add(Player(1, 100, player1Controller))
        players.add(Player(2, 100, player2Controller))
        val gameHand = GameHand(players, 10, 20)

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

    @Test
    fun playGameSplitPot() {
        val players = LinkedList<Player>()
        val player1Controller = PlayerControllerBettingDecisionFifo()
        val player2Controller = PlayerControllerBettingDecisionFifo()
        players.add(Player(1, 100, player1Controller))
        players.add(Player(2, 100, player2Controller))
        val gameHand = GameHand(players, 10, 20)

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