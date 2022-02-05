package poklin.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.inject.Guice
import com.google.inject.Singleton
import dev.misfitlabs.kotlinguice4.KotlinModule
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import poklin.GameProperties
import poklin.Player
import poklin.compose.state.PlayerState
import poklin.compose.state.TableState
import poklin.controler.PokerController
import poklin.controler.player.AskPlayerController
import poklin.dependencyinjection.TexasModule
import poklin.model.bet.BettingDecision
import poklin.model.bet.BettingDecision.BettingAction.NONE
import poklin.utils.ILogger
import poklin.utils.TableStateLogger

@Composable
fun PokerGame() {
    val gameProperties = GameProperties(15, 20, 10)
    gameProperties.addPlayer(
        Player(
            1,
            1000,
            AskPlayerController()
        )
    ) // AskPlayerController
    gameProperties.addPlayer(
        Player(
            2,
            1000,
            AskPlayerController() // PlayerControllerNormal()
        )
    )

    var injector = Guice.createInjector(TexasModule(), object : KotlinModule() {
        override fun configure() {
            bind<ILogger>().to<TableStateLogger>().`in`<Singleton>()
            bind<GameProperties>().toInstance(gameProperties)
        }
    })
    val pokerController = injector.getInstance(PokerController::class.java)

    var play by mutableStateOf(false)
    val tableState = remember { pokerController.tableState }

    val scrollState = rememberScrollState()
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
                            GlobalScope.launch {
                                pokerController.play()
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
                }
                Column(
                    modifier = Modifier.verticalScroll(
                        state = scrollState,
                        reverseScrolling = true
                    )
                ) {
                    Text("${tableState.log}")
                }
            }
        }
    }
}

@Composable
fun sharedCards(tableState: TableState) {
    NormalCards(tableState.sharedCard1)
    NormalCards(tableState.sharedCard2)
    NormalCards(tableState.sharedCard3)
    NormalCards(tableState.sharedCard4)
    NormalCards(tableState.sharedCard5)
}

@Composable
fun PlayerInfo(
    playerState: PlayerState
) {
    Column {
        Row {
            Text(
                playerState.name,
                modifier = Modifier.width(120.dp)
            )
            Text(
                "money:" + playerState.money,
                modifier = Modifier.width(100.dp)
            )
            Text(
                "/ (pot:${playerState.moneyPutInPot})",
                modifier = Modifier.width(100.dp)
            )
            if (playerState.bettingDecision != NONE) {
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

@Composable
fun BettingChoice(playerStates: SnapshotStateList<PlayerState>) {
    playerStates.forEach { playerState ->
        if (playerState.waitForDecision) {
            BettingChoiceButton(playerState)
        }
    }
}

@Composable
fun BettingChoiceButton(playerState: PlayerState) {
    Column {
        Row {
            Text(
                "" + playerState.seat + ":",
                modifier = Modifier.width(20.dp)
            )
            Text(
                playerState.name,
                modifier = Modifier.width(120.dp)
            )
            BetButton(
                text = "Fold",
                onClick = {
                    playerState.bettingDecision = BettingDecision.BettingAction.FOLD
                    playerState.waitForDecision = false
                }
            )
            if (playerState.canCheck) {
                BetButton(
                    text = "Check",
                    onClick = {
                        playerState.bettingDecision = BettingDecision.BettingAction.CHECK
                        playerState.waitForDecision = false
                    })
            } else {
                BetButton(
                    text = "Call",
                    onClick = {
                        playerState.bettingDecision = BettingDecision.BettingAction.CALL
                        playerState.waitForDecision = false
                    })
            }
        }
        Row {
            BetButton(
                text = "Rmin",
                onClick = {
                    playerState.bettingDecision = BettingDecision.BettingAction.RAISE
                    playerState.bettingAmount = 20
                    playerState.waitForDecision = false
                })
            var sliderBettingState by remember { mutableStateOf(20f) }
            BetButton(
                text = "Bet",
                onClick = {
                    playerState.bettingDecision = BettingDecision.BettingAction.RAISE
                    playerState.bettingAmount = sliderBettingState.toInt()
                    playerState.waitForDecision = false
                })
            Text(
                text = "${sliderBettingState.toInt()}$",
                modifier = Modifier.width(60.dp).align(alignment = Alignment.CenterVertically)
            )
            Slider(value = sliderBettingState, modifier = Modifier.fillMaxWidth().padding(8.dp),
                valueRange = playerState.minBettingAmount.toFloat()..playerState.money.toFloat(),
                steps = 100,
                colors = SliderDefaults.colors(thumbColor = MaterialTheme.colors.secondary),
                onValueChange = { newValue ->
                    sliderBettingState = newValue
                }
            )
        }
    }
}

@Composable
fun BetButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(30.dp).width(80.dp).padding(1.dp),
    ) {
        Text(text, fontSize = 12.sp)
    }
}

@Preview
@Composable
fun BetButtonPreview() {
    MaterialTheme(colors = darkColors()) {
        BetButton(
            text = "Check",
            onClick = {}
        )
    }
}

@Composable
fun MainScreenButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.padding(8.dp)
    ) {
        Text(title)
    }
}
