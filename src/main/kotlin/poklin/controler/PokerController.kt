package poklin.controler

import poklin.utils.ILogger
import poklin.GameProperties
import poklin.Games
import poklin.compose.state.TableState
import javax.inject.Inject

class PokerController @Inject constructor(
    private val gameController: GameController,
    private val logger: ILogger, private val gameProperties: GameProperties
) {
    val tableState = TableState

    init {
        gameProperties.players.forEach { tableState.playersState.add(it.playerState) }
    }

    private val games = Games(gameProperties.players)
    fun play() {
        var i = 0
        while (i < gameProperties.numberOfHands && gameProperties.nbPlayersWithMoney > 1) {
            gameController.play(games)
            games.setNextDealer()
            i++
        }
        printFinalStats()
    }

    private fun printFinalStats() {
        logger.log("-----------------------------------------")
        logger.log("Statistics")
        logger.log("-----------------------------------------")
        logger.log("Number of hands played: " + games.gamesCount())
        for (player in games.players) {
            logger.log(
                player.info() + " - " + player.playerController.toString().padEnd(8) + " : " + player
                    .money + "$"
            )
        }
    }
}