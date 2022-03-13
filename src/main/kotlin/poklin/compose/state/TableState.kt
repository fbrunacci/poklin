package poklin.compose.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.inject.Inject
import poklin.GameProperties
import poklin.model.bet.BettingDecision.BettingAction.NONE

class TableState {

    fun newGame(dealerSeat: Int) {
        pot = 0f
        sharedCard1 = ""
        sharedCard2 = ""
        sharedCard3 = ""
        sharedCard4 = ""
        sharedCard5 = ""
        playersState.forEach { state ->
            state.moneyPutInPot = 0f
            state.bettingDecision = NONE
            state.bettingAmount = 0f
            state.card1 = ""
            state.card2 = ""
            state.dealer = dealerSeat == state.seat
        }
    }

    var pot by mutableStateOf(0f)
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

    fun highestBet(): Float {
        return playersState.maxOfOrNull { it.bettingAmount } ?: 0f
    }
}
