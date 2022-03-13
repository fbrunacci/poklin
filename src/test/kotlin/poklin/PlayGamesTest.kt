package poklin

import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.Injector
import org.junit.Before
import org.junit.Test
import poklin.controler.PokerController
import poklin.controler.player.PlayerControllerBluff
import poklin.controler.player.PlayerControllerNormal
import poklin.injection.ConsoleLoggerModule
import poklin.injection.TexasModule
import poklin.injection.toModule

internal class PlayGamesTest {

    lateinit var injector: Injector
    lateinit var gameProperties: GameProperties

    @Before
    @Throws(Exception::class)
    fun setUp() {
        gameProperties = GameProperties(20f, 10f, 1)
        with(gameProperties) {
            addPlayer(Player(1, 1000f, PlayerControllerBluff()))
            addPlayer(Player(2, 1000f, PlayerControllerBluff()))
            addPlayer(Player(3, 1000f, PlayerControllerNormal()))
            addPlayer(Player(4, 1000f, PlayerControllerNormal()))
            addPlayer(Player(5, 1000f, PlayerControllerNormal()))
        }
        injector = Guice.createInjector(TexasModule(), ConsoleLoggerModule(), gameProperties.toModule())
        injector.injectMembers(this)
    }

    @Inject
    lateinit var pokerController: PokerController

    @Test
    fun play10Round() {
        for (i in 1..10) {
            pokerController.play(15)

            val playerWithMoney = gameProperties.players.filter { player -> player.money > 0 }
            if (playerWithMoney.size == 1) {
                gameProperties.players.forEach { player -> player.money = 1000f }
            }
        }
    }
}