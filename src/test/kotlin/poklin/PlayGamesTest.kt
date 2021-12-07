package poklin

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Singleton
import dev.misfitlabs.kotlinguice4.KotlinModule
import org.junit.Before
import org.junit.Test
import poklin.controler.GameHandController
import poklin.controler.PokerController
import poklin.controler.player.PlayerControllerBluff
import poklin.controler.player.PlayerControllerNormal
import poklin.dependencyinjection.TexasModule
import poklin.utils.ConsoleLogger
import poklin.utils.ILogger

class PlayGamesTest {

    lateinit var injector: Injector
    lateinit var gameProperties: GameProperties

    @Before
    @Throws(Exception::class)
    fun setUp() {
        gameProperties = GameProperties(15, 20, 10)
        with(gameProperties) {
            addPlayer(Player(1, 1000, PlayerControllerBluff()))
            addPlayer(Player(2, 1000, PlayerControllerBluff()))
            addPlayer(Player(3, 1000, PlayerControllerNormal()))
            addPlayer(Player(4, 1000, PlayerControllerNormal()))
            addPlayer(Player(5, 1000, PlayerControllerNormal()))
        }
        injector = Guice.createInjector(TexasModule(), object : KotlinModule() {
            override fun configure() {
                bind<ILogger>().to<ConsoleLogger>().`in`<Singleton>()
                bind<GameProperties>().toInstance(gameProperties)
            }}
        )
    }

    @Test
    fun play10Round() {
        GameHandController.DD = 0
        for (i in 1..10) {
            val pokerController = injector.getInstance(PokerController::class.java)
            pokerController.play()

            val playerWithMoney = gameProperties.players.filter { player -> player.money > 0 }
            if (playerWithMoney.size == 1) {
                gameProperties.players.forEach { player -> player.money = 1000 }
            }
        }
    }
}