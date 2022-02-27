package poklin.injection

import dev.misfitlabs.kotlinguice4.KotlinModule
import poklin.GameProperties

class GamePropertiesModule(val gameProperties: GameProperties) : KotlinModule() {
    override fun configure() {
        bind<GameProperties>().toInstance(gameProperties)
    }
}

fun GameProperties.toModule(): GamePropertiesModule {
    return GamePropertiesModule(this)
}
