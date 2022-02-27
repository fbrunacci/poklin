package poklin.injection

import com.google.inject.Singleton
import dev.misfitlabs.kotlinguice4.KotlinModule
import poklin.GameProperties
import poklin.utils.ConsoleLogger
import poklin.utils.ILogger

class ConsoleLoggerModule : KotlinModule() {
    override fun configure() {
        bind<ILogger>().to<ConsoleLogger>().`in`<Singleton>()
    }
}
