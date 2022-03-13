package poklin.compose.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import poklin.model.bet.BettingDecision.BettingAction

class PlayerState(val name: String, val seat: Int) {
    var money by mutableStateOf(0f)

    var dealer by mutableStateOf(false)

    var bettingDecision by mutableStateOf(BettingAction.NONE)
    var bettingAmount by mutableStateOf(0f)
    var moneyPutInPot by mutableStateOf(0f)

    var minBettingAmount by mutableStateOf(0f)
    var sliderBettingState by mutableStateOf(20f)

    var canCheck by mutableStateOf(false)

    var card1 by mutableStateOf("")
    var card2 by mutableStateOf("")

    var waitForDecision by mutableStateOf(false)
    var progress by mutableStateOf(0.0f)
}
