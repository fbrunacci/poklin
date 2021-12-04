package poklin.dependencyinjection

import com.google.inject.Singleton
import dev.misfitlabs.kotlinguice4.KotlinModule
import poklin.controler.GameHandController
import poklin.controler.HandStrengthEvaluator
import poklin.controler.PokerController
import poklin.controler.StatisticsController
import poklin.persistence.PersistenceModule

class TexasModule : KotlinModule() {
    override fun configure() {
        bind<PokerController>().`in`<Singleton>()
        bind<GameHandController>().`in`<Singleton>()
        bind<StatisticsController>().`in`<Singleton>()
        bind<HandStrengthEvaluator>().`in`<Singleton>()
    }
}