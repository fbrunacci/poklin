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
import poklin.utils.TableStateLogger
import java.io.File
import java.nio.file.Files

@Composable
fun ParsePokerGame() {
    var play by mutableStateOf(false)
    val tableState = remember { TableState() }
    val tableStateLogger = TableStateLogger(tableState)
    val logScrollState = rememberScrollState()

    val parser = AbsParser(tableState, tableStateLogger)
    val url = AbsParser.javaClass.getResource("aa_sample.txt")
    val f = File(url.toURI())

    MaterialTheme(colors = darkColors()) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Row {
                Column(
                    modifier = Modifier.width(500.dp)
                ) {
                    MainScreenButton(
                        title = "Play!",
                        enabled = !play,
                        onClick = {
                            play = true
                            Files.lines(f.toPath()).use { linesStream ->
                                linesStream.forEach { line: Any? ->
                                    parser.parseLine(
                                        (line as String?)!!
                                    )
                                }
                            }
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


