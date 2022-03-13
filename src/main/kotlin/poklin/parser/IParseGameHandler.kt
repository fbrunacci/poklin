package poklin.parser

import poklin.model.cards.Card

interface IParseGameHandler {
    fun holeCards()
    fun setFlop(parseCard: Card, parseCard1: Card, parseCard2: Card)
    fun setNextPlayerAction(playerName: String, bettingDecision: Any)
    fun addPlayer(name: String?, seat: Int, chips: Float, dealer : Boolean)
    fun setBigBlind(bb: Float)
    fun setSmallBlind(fl: Float)
    fun setButton(toInt: Int)
}
