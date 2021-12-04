package poklin.dependencyinjection

import com.google.inject.Singleton
import dev.misfitlabs.kotlinguice4.KotlinModule
import poklin.controler.*
import poklin.dependencyinjection.preflopsim.PreFlopSimulatorModule

class ControllerModule : KotlinModule() {
    override fun configure() {
        install(PreFlopSimulatorModule())
        bind<PokerController>().`in`<Singleton>()
        bind<GameHandController>().`in`<Singleton>()
        bind<HandPowerRanker>().`in`<Singleton>()
        bind<StatisticsController>().`in`<Singleton>()
        bind<HandStrengthEvaluator>().`in`<Singleton>()
        bind<EquivalenceClassController>().`in`<Singleton>()
        bind<OpponentModeler>().`in`<Singleton>()

//        bind(PlayerControllerPhaseINormal.class).in(Singleton.class);
//        bind(PlayerControllerPhaseIBluff.class).in(Singleton.class);
//        bind(PlayerControllerPhaseIINormal.class).in(Singleton.class);
//        bind(PlayerControllerPhaseIIBluff.class).in(Singleton.class);
//        bind(PlayerControllerPhaseIIIAgressive.class).in(Singleton.class);
//        bind(PlayerControllerPhaseIIIConservative.class).in(Singleton.class);
    }
}