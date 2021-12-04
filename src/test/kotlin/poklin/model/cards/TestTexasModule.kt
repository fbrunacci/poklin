package poklin.model.cards

import dev.misfitlabs.kotlinguice4.KotlinModule
import poklin.dependencyinjection.TexasModule
import poklin.persistence.PersistenceModule

class TestTexasModule : KotlinModule() {
    override fun configure() {
        install(TexasModule())
        install(PersistenceModule())
    }
}