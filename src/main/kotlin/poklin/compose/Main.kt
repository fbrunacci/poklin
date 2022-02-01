package poklin.compose

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication(
    title = "Poklin", state = WindowState(size = DpSize(1200.dp, 800.dp))
) {
    PokerGame()
}