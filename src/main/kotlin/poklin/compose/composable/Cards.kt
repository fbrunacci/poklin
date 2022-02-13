package poklin.compose.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun MiniCards(card: String) {
    Cards("minicards", card, Modifier.width(20.dp).height(30.dp).padding(1.dp))
}

@Composable
fun NormalCards(card: String) {
    Cards("cards", card, Modifier.width(60.dp).height(90.dp).padding(1.dp))
}

@Composable
fun Cards(folder: String, card: String, modifier: Modifier = Modifier) {
    if (card.isEmpty()) {
        Image(
            painter = painterResource("${folder}/back.png"),
            modifier = modifier,
            contentDescription = "$card"
        )
    } else {
        Image(
            painter = painterResource("${folder}/${card}.png"),
            modifier = modifier,
            contentDescription = "$card"
        )
    }
}

