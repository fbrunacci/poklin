package poklin.controler

import poklin.utils.ILogger
import poklin.GameProperties
import poklin.Game
import javax.inject.Inject

class PokerController @Inject constructor(
    private val gameHandController: GameHandController,
    private val logger: ILogger, private val gameProperties: GameProperties
) {
    private val game = Game(gameProperties.players)
    fun play() {
        var i = 0
        while (i < gameProperties.numberOfHands && gameProperties.nbPlayersWithMoney > 1) {
            gameHandController.play(game)
            game.setNextDealer()
            i++
        }
        printFinalStats()
    }

    private fun printFinalStats() {
        logger.log("-----------------------------------------")
        logger.log("Statistics")
        logger.log("-----------------------------------------")
        logger.log("Number of hands played: " + game.gameHandsCount())
        for (player in game.players) {
            logger.log(
                player.info() + " - " + player.playerController.toString().padEnd(8) + " : " + player
                    .money + "$"
            )
        }
    }
}