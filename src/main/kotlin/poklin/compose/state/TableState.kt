package poklin.compose.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import poklin.GameProperties
import poklin.model.bet.BettingDecision.BettingAction.NONE

object TableState {

    fun newGame(gameProperties: GameProperties) {
        pot = 0
        sharedCard1 = ""
        sharedCard2 = ""
        sharedCard3 = ""
        sharedCard4 = ""
        sharedCard5 = ""
        playersState.forEach { state ->
            state.moneyPutInPot = 0
            state.bettingDecision = NONE
            state.bettingAmount = 0
            state.card1 = ""
            state.card2 = ""
            state.dealer = gameProperties.dealerSeat == state.seat
        }
    }

    var pot by mutableStateOf(0)
    val playersState = mutableStateListOf<PlayerState>()

    var log by mutableStateOf("")

    var sharedCard1 by mutableStateOf("")
    var sharedCard2 by mutableStateOf("")
    var sharedCard3 by mutableStateOf("")
    var sharedCard4 by mutableStateOf("")
    var sharedCard5 by mutableStateOf("")

    fun playerStateAtSeat(seat: Int): PlayerState {
        return playersState.first { playerState -> playerState.seat == seat }
    }

    fun highestBet(): Int {
        return playersState.maxOf { it.bettingAmount }
    }
}
