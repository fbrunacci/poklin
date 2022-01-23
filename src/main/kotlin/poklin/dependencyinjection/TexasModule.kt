package poklin.dependencyinjection

import com.google.inject.Singleton
import dev.misfitlabs.kotlinguice4.KotlinModule
import poklin.controler.GameController
import poklin.controler.HandStrengthEvaluator
import poklin.controler.PokerController
import poklin.controler.StatisticsController

class TexasModule : KotlinModule() {
    override fun configure() {
        bind<PokerController>().`in`<Singleton>()
        bind<GameController>().`in`<Singleton>()
        bind<StatisticsController>().`in`<Singleton>()
        bind<HandStrengthEvaluator>().`in`<Singleton>()
    }
}