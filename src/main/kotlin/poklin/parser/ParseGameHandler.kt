package poklin.parser

import poklin.compose.state.PlayerState
import poklin.compose.state.TableState
import poklin.model.cards.Card

class ParseGameHandler(val tableState: TableState) : IParseGameHandler {

    override fun holeCards() {
    }

    override fun setFlop(parseCard: Card, parseCard1: Card, parseCard2: Card) {

    }

    override fun setNextPlayerAction(playerName: String, bettingDecision: Any) {

    }

    override fun addPlayer(name: String, seat: Int, chips: Float, dealer : Boolean) {
        val playerState = PlayerState(name, seat)
        playerState.dealer = dealer
        playerState.money = chips
        tableState.playersState.add(playerState)
    }

    override fun setBigBlind(bb: Float) {
    }

    override fun setSmallBlind(fl: Float) {

    }

    override fun setButton(toInt: Int) {
//        tableState.set
    }
}