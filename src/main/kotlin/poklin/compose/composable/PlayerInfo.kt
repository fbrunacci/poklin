package poklin.compose.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import poklin.compose.state.PlayerState
import poklin.model.bet.BettingDecision

@Composable
fun PlayerInfo(
    playerState: PlayerState
) {
    Column(
        Modifier.background(
            color = if (playerState.waitForDecision) {
                MaterialTheme.colors.primary
            } else {
                MaterialTheme.colors.background
            },
            shape = RoundedCornerShape(20)
        )
    ) {
        Row {
            Coins(playerState.dealer)
            Text(
                playerState.name,
                modifier = Modifier.width(120.dp)
            )
            Text(
                "money:${playerState.money}",
                modifier = Modifier.width(100.dp)
            )
            Text(
                "/ (pot:${playerState.moneyPutInPot})",
                modifier = Modifier.width(100.dp)
            )
            if (playerState.bettingDecision != BettingDecision.BettingAction.NONE) {
                Text(
                    "Dec:${playerState.bettingDecision}",
                    modifier = Modifier.width(100.dp)
                )
            }
        }
        Row {
            Row {
                MiniCards(playerState.card1)
                MiniCards(playerState.card2)
            }
        }
    }
}

@Composable
fun Coins(dealer: Boolean, modifier : Modifier = Modifier.width(20.dp).height(20.dp).padding(1.dp)) {
    if (dealer) {
        Image(
            painter = painterResource("coins/dollars.png"),
            modifier = modifier,
            contentDescription = "dealer"
        )
    } else {
        Image(
            painter = painterResource("coins/empty.png"),
            modifier = modifier,
            contentDescription = "empty"
        )
    }
}
