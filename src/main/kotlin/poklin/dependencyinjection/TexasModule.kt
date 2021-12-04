package poklin.dependencyinjection

import dev.misfitlabs.kotlinguice4.KotlinModule
import poklin.persistence.PersistenceModule

class TexasModule : KotlinModule() {
    override fun configure() {
        install(ControllerModule())
//        install(PersistenceModule())
    }
}