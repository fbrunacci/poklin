package poklin

import com.google.inject.Guice
import com.google.inject.Singleton
import dev.misfitlabs.kotlinguice4.KotlinModule
import org.junit.Before
import org.junit.Test
import poklin.PlayRoundTest.TestGamePropertiesModule.TestGameProperties
import poklin.controler.GameController
import poklin.dependencyinjection.TexasModule
import poklin.model.bet.BettingDecision
import poklin.model.bet.BettingDecision.Companion.RAISE_CUSTOM
import poklin.model.cards.Cards
import poklin.model.cards.DeckFifo
import poklin.utils.ConsoleLogger
import poklin.utils.ILogger

internal class PlayRoundTest {

    class TestGamePropertiesModule : KotlinModule() {
        override fun configure() {
            bind<GameProperties>().to<TestGameProperties>().`in`<Singleton>()
            bind<ILogger>().to<ConsoleLogger>().`in`<Singleton>()
        }

        class TestGameProperties : GameProperties(20, 10, dealerSeat = 1) {
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

    lateinit var gameController: GameController
    lateinit var testGameProperties: TestGameProperties

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val injector = Guice.createInjector(TexasModule(), TestGamePropertiesModule())
        gameController = injector.getInstance(GameController::class.java)
        testGameProperties = injector.getInstance(GameProperties::class.java) as TestGameProperties
    }

    @Test
    fun playRound() {
        val game = Game(testGameProperties.players, 10, 20, dealerSeat = 1)
        with(testGameProperties) {
            player2Controller.pushDecision(RAISE_CUSTOM(50))
            player1Controller.pushDecision(BettingDecision.CALL)
            player2Controller.pushDecision(BettingDecision.FOLD)
        }
        gameController.play(game)

        with(testGameProperties) {
            println("=====================")
            println(player1)
            println(player2)
        }
    }

    @Test
    fun playRoundS1() {
        val game = Game(testGameProperties.players, 10, 20, dealerSeat = 1)
        with(testGameProperties) {
            player2Controller.pushDecision(RAISE_CUSTOM(50))
            player1Controller.pushDecision(BettingDecision.CALL)
            gameController.playRound(game)
            player2Controller.pushDecision(BettingDecision.FOLD)
            gameController.playRound(game)
        }
    }

    @Test
    fun playRoundS2() {
        val game = Game(testGameProperties.players, 10, 20, dealerSeat = 1)
        with(testGameProperties) {
            player2Controller.pushDecision(RAISE_CUSTOM(50))
            player1Controller.pushDecision(BettingDecision.CALL)
            gameController.playRound(game)
            player2Controller.pushDecision(BettingDecision.CHECK)
            player1Controller.pushDecision(BettingDecision.FOLD)
            gameController.playRound(game)
        }
    }

    @Test
    fun playRoundS3() {
        val game = Game(testGameProperties.players, 10, 20, dealerSeat = 1)
        with(testGameProperties) {
            player2Controller.pushDecision(RAISE_CUSTOM(50))
            player1Controller.pushDecision(BettingDecision.CALL)
            gameController.playRound(game)
            player2Controller.pushDecision(BettingDecision.CHECK)
            player1Controller.pushDecision(BettingDecision.CHECK)
            gameController.playRound(game)
            player2Controller.pushDecision(BettingDecision.CHECK)
            player1Controller.pushDecision(BettingDecision.CHECK)
            gameController.playRound(game)
            player2Controller.pushDecision(BettingDecision.CHECK)
            player1Controller.pushDecision(BettingDecision.CHECK)
            gameController.playRound(game)
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
        val deckFifo = DeckFifo()
        val game = Game(testGameProperties.players, 10, 20, deckFifo, dealerSeat = 1)
        Cards.fromText("As Ks Qs Js 10s 9s 8s 7s 6s 5s 4s 3s 2s")
            .forEach { card -> deckFifo.pushCardToTop(card) }

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
            gameController.play(game)

            println("=====================")
            println(player1)
            println(player2)
        }
    }

    @Test
    fun playGameS4() {
        val game = Game(testGameProperties.players, 10, 20, dealerSeat = 1)
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
            gameController.play(game)
        }
    }

    @Test
    fun playGameSplitPot() {
        val game = Game(testGameProperties.players, 10, 20, dealerSeat = 1)
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
            gameController.play(game)
        }
    }
}