package poklin.controler

import poklin.utils.ILogger
import poklin.GameProperties
import poklin.Games
import poklin.compose.state.TableState
import javax.inject.Inject

class PokerController @Inject constructor(
    private val gameController: GameController,
    private val gameProperties: GameProperties,
    private val logger: ILogger
) {

    val tableState = TableState

    init {
        gameProperties.players.forEach { tableState.playersState.add(it.playerState) }
    }

    fun play() {
        var i = 0
        while (gameProperties.nbPlayersWithMoney > 1) {
            gameController.play()
            i++
        }
        printFinalStats()
    }

    fun play(numberOfHands : Int) {
        var i = 0
        while (i < numberOfHands && gameProperties.nbPlayersWithMoney > 1) {
            gameController.play()
            i++
        }
        printFinalStats()
    }

    private fun printFinalStats() {
        logger.log("-----------------------------------------")
        logger.log("Statistics")
        logger.log("-----------------------------------------")
//        logger.log("Number of hands played: " + games.gamesCount())
        for (player in gameProperties.players) {
            logger.log(
                player.info() + " - " + player.playerController.toString().padEnd(8) + " : " + player
                    .money + "$"
            )
        }
    }
}