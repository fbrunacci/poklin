package poklin.dependencyinjection.preflopsim

import com.google.inject.Inject
import poklin.Game
import poklin.GameHand
import poklin.GameProperties
import poklin.controler.GameHandController
import poklin.controler.HandStrengthEvaluator
import poklin.controler.OpponentModeler
import poklin.controler.StatisticsController
import poklin.HandPowerRanker
import poklin.model.bet.BettingRoundName
import poklin.model.cards.EquivalenceClass
import poklin.utils.ILogger

class GameHandControllerPreFlopRoll @Inject constructor(
    logger: ILogger,
    val gameProperties: GameProperties,
    statisticsController: StatisticsController,
    handStrengthEvaluator: HandStrengthEvaluator,
    opponentModeler: OpponentModeler
) : GameHandController(
    logger,
    gameProperties,
    statisticsController,
    handStrengthEvaluator,
    opponentModeler
) {
    fun play(game: Game, equivalenceClass: EquivalenceClass?) {
        logger.log("-----------------------------------------")
        logger.log("Game Hand #" + (game.gameHandsCount() + 1))
        logger.log("-----------------------------------------")
        logger.log("-----------------------------------------")
        logger.log(equivalenceClass.toString())
        logger.log("-----------------------------------------")
        val gameHand = createGameHand(game, gameProperties, equivalenceClass)
        var haveWinner: Boolean? = false
        while (gameHand.bettingRoundName != BettingRoundName.POST_RIVER
            && !haveWinner!!
        ) {
            haveWinner = playRound(gameHand)
        }
        if (!haveWinner!!) {
            showDown(gameHand)
        }
    }

    private fun createGameHand(
        game: Game,
        gameProperties: GameProperties,
        equivalenceClass: EquivalenceClass?
    ): GameHand {
        val gameHand: GameHand = GameHandPreFlopRoll(
            game.players, gameProperties.smallBlind, gameProperties.bigBlind,
            equivalenceClass
        )
        game.addGameHand(gameHand)
        return gameHand
    }
}