package poklin.persistence

import com.google.inject.AbstractModule
import poklin.persistence.PreFlopPersistence
import javax.inject.Singleton

class PersistenceModule : AbstractModule() {
    override fun configure() {
        bind(PersistenceManager::class.java).`in`(Singleton::class.java)
        bind(PreFlopPersistence::class.java).`in`(Singleton::class.java)
    }
}