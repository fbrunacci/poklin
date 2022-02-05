package poklin.controler

import poklin.*
import poklin.compose.state.TableState
import poklin.model.HandPower
import poklin.model.bet.BettingDecision
import poklin.model.bet.BettingRoundName
import poklin.model.cards.Card
import poklin.model.cards.Deck
import poklin.utils.ILogger
import java.util.*
import javax.inject.Inject

open class GameController @Inject constructor(
    private val logger: ILogger,
    private val gameProperties: GameProperties,
    private val statisticsController: StatisticsController,
    private val handStrengthEvaluator: HandStrengthEvaluator,
) {

    fun play() {
        TableState.newGame()
        val game = createGame()
        logGame(game)
        play(game)
    }

    fun play(game: Game) {
        while (game.table.getActivePlayers().size > 1
            && game.bettingRoundName != BettingRoundName.POST_RIVER
        ) {
            playRound(game)
        }
        showDown(game)
    }

    private fun logGame(game: Game) {
        logger.log("-----------------------------------------")
//        logger.logImportant("Game Hand #" + (games.gamesCount() + 1))
        logger.log("-----------------------------------------")
        val iterator: Iterator<Player> = game.table.players.iterator()
        while (iterator.hasNext()) {
            val player = iterator.next()
            logger.log(player.toString())
        }
        logger.log("-----------------------------------------")
    }

    private fun createGame(): Game {
        val game = Game(
            gameProperties.players,
            gameProperties.smallBlind,
            gameProperties.bigBlind,
            Deck()
        )
        return game
    }

    /**
     * @param game
     * @return true if we have a winner
     */
    fun playRound(game: Game) {
        game.nextRound()
        val currentBettingRound = game.currentBettingRound
        logBettingRound(game)
        if (game.bettingRoundName == BettingRoundName.PRE_FLOP) {
            takeBlinds(game)
        }

        while (currentBettingRound.playerCanBet()) {
            val player = game.table.nextPlayer()
            val bettingDecision = player.decide(game)
            applyDecision(game, player, bettingDecision)
        }
    }

    private fun logBettingRound(game: Game) {
        var logMsg = "---" + game.bettingRoundName
        logMsg += " (" + game.table.getActivePlayers().size + " active players, "
        logMsg += game.table.getInGamePlayers().size.toString() + " players in game, "
        logMsg += game.totalBets.toString() + "$)"
        if (!game.sharedCards.isEmpty()) {
            logMsg += " " + game.sharedCards
        }
        logger.log(logMsg)
    }

    private fun takeBlinds(game: Game) {
        val smallBlindPlayer = game.table.nextPlayer()
        val bigBlindPlayer = game.table.nextPlayer()
        val smallBlind = Math.min(game.smallBlind, smallBlindPlayer!!.money)
        val bigBlind = Math.min(game.bigBlind, bigBlindPlayer!!.money)
        logger.log("$smallBlindPlayer: Small blind $smallBlind")
        logger.log("$bigBlindPlayer: Big blind $bigBlind")
        // TODO ALL IN ...
        game.currentBettingRound.placeBet(smallBlindPlayer, BettingDecision.BettingAction.SMALLBLIND, smallBlind)
        game.currentBettingRound.placeBet(bigBlindPlayer, BettingDecision.BettingAction.BIGBLIND, bigBlind)
    }

    private fun applyDecision(game: Game, player: Player, bettingDecision: BettingDecision) {
        TableState.playerStateAtSeat(player.seat)?.bettingDecision = bettingDecision.bettingAction
        val handStrength = handStrengthEvaluator.evaluate(
            player!!.holeCards, game.sharedCards,
            game.table.getActivePlayers().size
        )
        game.applyDecision(player, bettingDecision, gameProperties, handStrength)
        val bettingRound = game.currentBettingRound
        logger.log(
            player.toString() + ": " + bettingDecision + " "
                    + bettingRound.getBetForPlayer(player) + "$"
        )
        DD++
    }

    private fun getWinnersByHandPower(game: Game): Map<Int, List<Player>> {
        val activePlayers: List<Player> = game.table.getActivePlayers()
        val winners: MutableMap<Int, MutableList<Player>> = HashMap()
        if (activePlayers.size == 1) {
            val winner = activePlayers.first()
            return mapOf(winner.seat to listOf(winner))
        }
        val sharedCards: List<Card> = game.sharedCards
        for (player in activePlayers) {
            val mergeCards: MutableList<Card> = ArrayList(player.holeCards)
            mergeCards.addAll(sharedCards)
            val handPower = HandPowerRanker.rank(mergeCards)
            logger.log("$player $handPower")
            val handPowerValue = handPower.value
            if (!winners.containsKey(handPowerValue)) {
                winners[handPowerValue] = ArrayList()
            }
            winners[handPowerValue]!!.add(player)
        }
        val sortedWinners = TreeMap<Int, List<Player>>(Collections.reverseOrder())
        sortedWinners.putAll(winners)
        return sortedWinners
    }

    private fun getWinnersList(game: Game): List<Player> {
        val activePlayers: Iterable<Player> = game.table.getActivePlayers()
        val sharedCards: List<Card> = game.sharedCards
        var bestHandPower: HandPower? = null
        val winners: MutableList<Player> = ArrayList()
        for (player in activePlayers) {
            val mergeCards: MutableList<Card> = ArrayList(player.holeCards)
            mergeCards.addAll(sharedCards)
            val handPower = HandPowerRanker.rank(mergeCards)
            logger.log("$player: $handPower")
            if (bestHandPower == null || handPower.compareTo(bestHandPower) > 0) {
                winners.clear()
                winners.add(player)
                bestHandPower = handPower
            } else if (handPower.compareTo(bestHandPower) == 0) {
                winners.add(player)
            }
        }
        statisticsController.storeWinners(winners)
        return winners
    }

    /**
     * TODO https://www.pokerdictionary.net/poker-tips/understanding-side-pots/
     * Method for determining side pot(s) at then end of a betting round when one or more players have gone All-In:
     * - Determine the amount of each All-In bet (if there is more than one)
     * - Select the amount of the smallest All-In bet (if there is more than one)
     * - Deduct that amount from all the bets and add it to the current pot
     * - Close the current pot and move it off to the side (as a side pot)
     * - Start a new current pot
     * Repeat steps 1 to 5 if there are more All-In bets
     * Move the remaining bets to the current pot
     * If a side pot has only one player, the chips are returned to the player
     *
     * @param game
     */
    protected fun showDown(game: Game) {
        logger.log("--- Showdown")
        calculatePotDistribution(game)
    }

    fun calculatePotDistribution(game: Game) {
        // Per rank (single or multiple winners), calculate pot distribution.
        val totalPot = game.pot.totalPot
        val potDivision: MutableMap<Player, Int> = HashMap()
        val activePlayers = game.table.getActivePlayers()

        // TODO start from after dealer for odds
        val winnersMap = getWinnersByHandPower(game)
        for ((handPower, winners) in winnersMap) {
            for (pot in game.pot.getSidePots()) {
                // Determine how many winners share this pot.
                //logger.log(pot.toString()+" nb winner:"+pot.getContributors().size());
                var noOfWinnersInPot = 0
                for (winner in winners) {
                    if (pot.hasContributer(winner)) {
                        noOfWinnersInPot++
                    }
                }
                if (noOfWinnersInPot > 0) {
                    // Divide pot over winners.
                    val potShare = pot.value / noOfWinnersInPot
                    for (winner in winners) {
                        if (pot.hasContributer(winner)) {
                            val oldShare = potDivision[winner]
                            if (oldShare != null) {
                                potDivision[winner] = oldShare + potShare
                            } else {
                                potDivision[winner] = potShare
                            }
                        }
                    }
                    // Determine if we have any odd chips left in the pot.
                    var oddChips = pot.value % noOfWinnersInPot // TODO cas ou division < small blind

                    // Divide odd chips over winners, starting left of the dealer.
                    while (oddChips > 0) {
                        for (activePlayer in activePlayers) {
                            val oldShare = potDivision[activePlayer]
                            if (oldShare != null) {
                                potDivision[activePlayer] = oldShare + 1
                                logger.log("[DEBUG] $activePlayer receives an odd chip from the pot.")
                                oddChips--
                            }
                        }
                    }
                    pot.clear()
                }
            }
        }

        // Divide winnings.
        val winnerText = StringBuilder()
        var totalWon = 0
        for (winner in potDivision.keys) {
            val potShare = potDivision[winner]!!
            //winner.win(potShare);
            totalWon += potShare
            if (winnerText.length > 0) {
                winnerText.append(", ")
            }
            winnerText.append(String.format("%s wins $ %d", winner, potShare))
            winner.addMoney(potShare)
        }
        winnerText.append('.')
        logger.log(winnerText.toString())
    }

    companion object {
        var DD = 0
    }
}