package poklin.dependencyinjection.preflopsim

import dev.misfitlabs.kotlinguice4.KotlinModule
import javax.inject.Singleton

class PreFlopSimulatorModule : KotlinModule() {
    override fun configure() {
        bind<PlayerControllerPreFlopRoll>().`in`<Singleton>()
        bind<GameHandControllerPreFlopRoll>().`in`<Singleton>()
    }
}