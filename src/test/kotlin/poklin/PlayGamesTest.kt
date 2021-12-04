package poklin

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Singleton
import dev.misfitlabs.kotlinguice4.KotlinModule
import org.junit.Before
import org.junit.Test
import poklin.PlayGamesTest.TestGamePropertiesModule.TestGameProperties
import poklin.controler.GameHandController
import poklin.controler.PokerController
import poklin.controler.phase1.PlayerControllerPhaseIBluff
import poklin.controler.phase1.PlayerControllerPhaseINormal
import poklin.dependencyinjection.TexasModule
import poklin.utils.ConsoleLogger
import poklin.utils.ILogger

class PlayGamesTest {
    class TestGamePropertiesModule : KotlinModule() {
        override fun configure() {
            bind<GameProperties>().to<TestGameProperties>().`in`<Singleton>()
            bind<ILogger>().to<ConsoleLogger>().`in`<Singleton>()
        }
        class TestGameProperties : GameProperties(15, 1000, 20, 10) {
            init {
                addPlayer(Player(1, 1000, PlayerControllerPhaseIBluff()))
                addPlayer(Player(2, 1000, PlayerControllerPhaseIBluff()))
                addPlayer(Player(3, 1000, PlayerControllerPhaseINormal()))
                addPlayer(Player(4, 1000, PlayerControllerPhaseINormal()))
                addPlayer(Player(5, 1000, PlayerControllerPhaseINormal()))
            }
        }
    }

    lateinit var injector: Injector
    lateinit var gameHandController: GameHandController
    lateinit var testGameProperties: TestGameProperties

    @Before
    @Throws(Exception::class)
    fun setUp() {
        injector = Guice.createInjector(TexasModule(), TestGamePropertiesModule())
        gameHandController = injector.getInstance(GameHandController::class.java)
        testGameProperties = injector.getInstance(GameProperties::class.java) as TestGameProperties
    }

    @Test
    fun play100Games() {
        for (i in 0..9) {
            GameHandController.DD = 0
            val pokerController = injector.getInstance(PokerController::class.java)
            pokerController.play()
        }
    }
}