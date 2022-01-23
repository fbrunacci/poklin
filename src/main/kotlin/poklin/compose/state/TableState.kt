package poklin.compose.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import poklin.model.bet.BettingDecision.BettingAction.NONE

object TableState {

    fun newGame() {
        pot = 0
        sharedCard1 = ""
        sharedCard2 = ""
        sharedCard3 = ""
        sharedCard4 = ""
        sharedCard5 = ""
        players.forEach { player ->
            player.moneyPutInPot = 0
            player.bettingDecision = NONE
            player.bettingAmount = 0
            player.card1 = ""
            player.card2 = ""
        }
    }

    var pot by mutableStateOf(0)
    val players = mutableStateListOf<PlayerState>()

    var sharedCard1 by mutableStateOf("")
    var sharedCard2 by mutableStateOf("")
    var sharedCard3 by mutableStateOf("")
    var sharedCard4 by mutableStateOf("")
    var sharedCard5 by mutableStateOf("")

    fun playerStateAtSeat(seat: Int): PlayerState {
        return players.first { playerState -> playerState.seat == seat }
    }
}
