package poklin

import com.google.inject.Guice
import com.google.inject.Singleton
import dev.misfitlabs.kotlinguice4.KotlinModule
import poklin.PlayVs.TestGamePropertiesModule.TestGameProperties
import poklin.controler.GameHandController
import poklin.controler.PokerController
import poklin.controler.player.AskPlayerController
import poklin.controler.player.PlayerControllerBluff
import poklin.dependencyinjection.TexasModule
import poklin.utils.ConsoleLogger
import poklin.utils.ILogger

fun main(args : Array<String>) {
    PlayVs().playVsRound()
}

class PlayVs {

    class TestGamePropertiesModule : KotlinModule() {
        override fun configure() {
            bind<GameProperties>().to<TestGameProperties>().`in`<Singleton>()
            bind<ILogger>().to<ConsoleLogger>().`in`<Singleton>()
        }

        class TestGameProperties : GameProperties(15, 20, 10) {
            init {
                addPlayer(Player(1, 1000, AskPlayerController()))
                addPlayer(Player(2, 1000, PlayerControllerBluff()))
            }
        }
    }

    fun playVsRound() {
        var injector = Guice.createInjector(TexasModule(), TestGamePropertiesModule())
        var gameHandController = injector.getInstance(GameHandController::class.java)
        var testGameProperties = injector.getInstance(GameProperties::class.java) as TestGameProperties
        GameHandController.DD = 0
        for (i in 1..10) {
            val pokerController = injector.getInstance(PokerController::class.java)
            pokerController.play()

            val playerWithMoney = testGameProperties.players.filter { player -> player.money > 0 }
            if (playerWithMoney.size == 1) {
                testGameProperties.players.forEach { player -> player.money = 1000 }
            }
        }
    }
}