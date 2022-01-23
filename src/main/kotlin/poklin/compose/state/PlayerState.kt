package poklin.compose.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import poklin.model.bet.BettingDecision.BettingAction

class PlayerState(val name: String, val seat : Int) {
    var money by mutableStateOf(0)

    var bettingDecision by mutableStateOf(BettingAction.NONE)
    var bettingAmount by mutableStateOf(0)
    var moneyPutInPot by mutableStateOf(0)

    var canCheck by mutableStateOf(false)

    val dealer by mutableStateOf(false)
    var card1 by mutableStateOf("")
    var card2 by mutableStateOf("")

    var waitForDecision by mutableStateOf(false)
}
