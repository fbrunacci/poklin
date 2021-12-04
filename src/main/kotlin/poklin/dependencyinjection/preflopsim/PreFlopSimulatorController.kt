package poklin.dependencyinjection.preflopsim

import poklin.Game
import poklin.GameProperties
import poklin.Player
import poklin.controler.EquivalenceClassController
import poklin.controler.StatisticsController
import poklin.persistence.PreFlopPersistence
import poklin.utils.ILogger
import javax.inject.Inject

class PreFlopSimulatorController @Inject constructor(
    private val logger: ILogger, private val gameProperties: GameProperties,
    private val playerControllerPreFlopRoll: PlayerControllerPreFlopRoll,
    private val equivalenceClassController: EquivalenceClassController,
    private val gameHandControllerPreFlopRoll: GameHandControllerPreFlopRoll,
    private val statisticsController: StatisticsController,
    private val preFlopPersistence: PreFlopPersistence
) {
    private val game = Game()
    fun play() {
        equivalenceClassController.generateAllEquivalenceClass()
        game.addPlayer(Player(1, gameProperties.initialMoney, playerControllerPreFlopRoll))
        val equivalenceClasses = equivalenceClassController.equivalenceClasses
        for (numberOfPlayers in 2..10) {
            game.addPlayer(Player(numberOfPlayers, 0, playerControllerPreFlopRoll))
            for (equivalenceClass in equivalenceClasses) {
                statisticsController.initializeStatistics()
                for (i in 0 until ROLLOUTS_PER_EQUIV_CLASS) {
                    gameHandControllerPreFlopRoll.play(game, equivalenceClass)
                    game.setNextDealer()
                }
                val percentageWin = statisticsController.player1Wins.toDouble() / ROLLOUTS_PER_EQUIV_CLASS
                preFlopPersistence.persist(numberOfPlayers, equivalenceClass, percentageWin)
                logger.logImportant("=================")
                logger.logImportant(
                    "STATISTICS FOR EQUIVALENCE CLASS "
                            + equivalenceClass.toString()
                )
                logger.logImportant("Number of hands played: " + ROLLOUTS_PER_EQUIV_CLASS)
                logger.logImportant("Number players: $numberOfPlayers")
                logger.logImportant("Percentage of wins is $percentageWin")
            }
        }
    }

    companion object {
        private const val ROLLOUTS_PER_EQUIV_CLASS = 100
    }
}