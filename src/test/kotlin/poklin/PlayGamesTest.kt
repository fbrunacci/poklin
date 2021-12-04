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
import poklin.controler.phase1.PlayerControllerBluff
import poklin.controler.phase1.PlayerControllerNormal
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
                addPlayer(Player(1, 1000, PlayerControllerBluff()))
                addPlayer(Player(2, 1000, PlayerControllerBluff()))
                addPlayer(Player(3, 1000, PlayerControllerNormal()))
                addPlayer(Player(4, 1000, PlayerControllerNormal()))
                addPlayer(Player(5, 1000, PlayerControllerNormal()))
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
    fun play100Round() {
        GameHandController.DD = 0
        for (i in 1..100) {
            val pokerController = injector.getInstance(PokerController::class.java)
            pokerController.play()

            val playerWithMoney = testGameProperties.players.filter { player -> player.money > 0 }
            if ( playerWithMoney.size == 1 ) {
                testGameProperties.players.forEach { player -> player.money = 1000 }
            }
        }
    }
}