package poklin.injection

import com.google.inject.Singleton
import dev.misfitlabs.kotlinguice4.KotlinModule
import poklin.compose.state.TableState
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
        bind<TableState>().`in`<Singleton>()
    }
}