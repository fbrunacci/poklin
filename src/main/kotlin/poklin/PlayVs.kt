package poklin

import com.google.inject.Guice
import com.google.inject.Singleton
import dev.misfitlabs.kotlinguice4.KotlinModule
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

    fun playVsRound() {
        val gameProperties = GameProperties(15, 20, 10)
        gameProperties.addPlayer(Player(1, 1000, AskPlayerController()))
        gameProperties.addPlayer(Player(2, 1000, PlayerControllerBluff()))

        var injector = Guice.createInjector(TexasModule(), object : KotlinModule() {
            override fun configure() {
                bind<ILogger>().to<ConsoleLogger>().`in`<Singleton>()
                bind<GameProperties>().toInstance(gameProperties)
            }}
        )

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