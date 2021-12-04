package poklin.dependencyinjection

import com.google.inject.Singleton
import dev.misfitlabs.kotlinguice4.KotlinModule
import poklin.HandPowerRanker
import poklin.controler.*
import poklin.dependencyinjection.preflopsim.PreFlopSimulatorModule

class ControllerModule : KotlinModule() {
    override fun configure() {
        install(PreFlopSimulatorModule())
        bind<PokerController>().`in`<Singleton>()
        bind<GameHandController>().`in`<Singleton>()
        bind<StatisticsController>().`in`<Singleton>()
        bind<HandStrengthEvaluator>().`in`<Singleton>()
        bind<OpponentModeler>().`in`<Singleton>()
    }
}