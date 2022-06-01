package poklin.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import poklin.compose.composable.PlayerInfo
import poklin.compose.state.TableState
import poklin.parser.AbsParser
import poklin.parser.ParseGameScanner
import poklin.utils.TableStateLogger
import java.io.File

@Composable
fun ParsePokerGame() {
    var hasNextLine by mutableStateOf(true)
    val tableState = remember { TableState() }
    val tableStateLogger = TableStateLogger(tableState)
    val logScrollState = rememberScrollState()

    val parser = AbsParser(tableState, tableStateLogger)
    val url = AbsParser.javaClass.getResource("aa_sample.txt")
    val file = File(url.toURI())
    val parseGameScanner = ParseGameScanner(file, parser)

    MaterialTheme(colors = darkColors()) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Row {
                Column(
                    modifier = Modifier.width(500.dp)
                ) {
                    MainScreenButton(
                        title = "Play!",
                        enabled = hasNextLine,
                        onClick = {
                            parseGameScanner.scanNextLine()
                            hasNextLine = parseGameScanner.hasNextLine()
                        }
                    )
                    Row {
                        sharedCards(tableState)
                        Text("pot: ${tableState.pot}")
                    }
                    tableState.playersState.forEach {
                        PlayerInfo(it)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    BettingChoice(tableState.playersState)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Column(
                    modifier = Modifier.verticalScroll(
                        state = logScrollState,
                        reverseScrolling = true
                    )
                )
                {
                    Text("${tableState.log}")
                }
            }
        }
    }
}


