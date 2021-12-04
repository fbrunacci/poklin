package poklin.controler

import poklin.Game
import poklin.GameHand
import poklin.GameProperties
import poklin.Player
import poklin.dependencyinjection.HandPowerRanker
import poklin.model.HandPower
import poklin.model.bet.BettingDecision
import poklin.model.bet.BettingRoundName
import poklin.model.cards.Card
import poklin.model.cards.Deck
import poklin.utils.ILogger
import javax.inject.Inject

open class GameHandController @Inject constructor(
    protected val logger: ILogger,
    private val handPowerRanker: HandPowerRanker,
    private val gameProperties: GameProperties,
    private val statisticsController: StatisticsController,
    private val handStrengthEvaluator: HandStrengthEvaluator,
    private val opponentModeler: OpponentModeler
) {
    fun play(game: Game) {
        logger.log("-----------------------------------------")
        logger.logImportant("Game Hand #" + (game.gameHandsCount() + 1))
        logger.log("-----------------------------------------")
        val gameHand = createGameHand(game)
        val iterator: Iterator<Player?> = gameHand.table.players.iterator()
        while (iterator.hasNext()) {
            val player = iterator.next()
            logger.log(player.toString())
        }
        logger.log("-----------------------------------------")
        play(gameHand)
    }

    fun play(gameHand: GameHand) {
        var haveWinner = false
        while (gameHand.bettingRoundName != BettingRoundName.POST_RIVER
            && !haveWinner
        ) {
            haveWinner = playRound(gameHand)
        }
        if (!haveWinner) {
            showDown(gameHand)
        }
    }

    private fun createGameHand(game: Game): GameHand {
        val gameHand = GameHand(
            game.players,
            gameProperties.smallBlind,
            gameProperties.bigBlind,
            Deck()
        ) // TODO adapt samll/big Blind
        game.addGameHand(gameHand)
        return gameHand
    }

    /**
     * @param gameHand
     * @return true if we have a winner
     */
    fun playRound(gameHand: GameHand): Boolean {
        gameHand.nextRound()
        val currentBettingRound = gameHand.currentBettingRound
        logBettingRound(gameHand)
        if (gameHand.bettingRoundName == BettingRoundName.PRE_FLOP) {
            takeBlinds(gameHand)
        }
        val numberOfPlayersAtBeginningOfRound = gameHand.table.getActivePlayers().size
        while (currentBettingRound.playerCanBet()) {
            val player = gameHand.table.nextPlayer()
            val bettingDecision = player!!.decide(gameHand)
            applyDecision(gameHand, player, bettingDecision)
        }

        //logger.log("Pots: "+gameHand.getPots().toString());

        // Check if we have a winner
        if (gameHand.table.getActivePlayers().size == 1) {
            val winner = gameHand.table.currentPlayer()
            winner!!.addMoney(gameHand.totalBets)
            logger.log(winner.toString() + ": WIN! +" + gameHand.totalBets + "$")
            return true
        }
        return false
    }

    private fun logBettingRound(gameHand: GameHand) {
        var logMsg = "---" + gameHand.bettingRoundName
        logMsg += " (" + gameHand.table.getActivePlayers().size + " active players, "
        logMsg += gameHand.table.getInGamePlayers().size.toString() + " players in game, "
        logMsg += gameHand.totalBets.toString() + "$)"
        if (!gameHand.sharedCards.isEmpty()) {
            logMsg += " " + gameHand.sharedCards
        }
        logger.log(logMsg)
    }

    private fun takeBlinds(gameHand: GameHand) {
        val smallBlindPlayer = gameHand.table.nextPlayer()
        val bigBlindPlayer = gameHand.table.nextPlayer()
        val smallBlind = Math.min(gameHand.smallBlind, smallBlindPlayer!!.money)
        val bigBlind = Math.min(gameHand.bigBlind, bigBlindPlayer!!.money)
        logger.log("$smallBlindPlayer: Small blind $smallBlind")
        logger.log("$bigBlindPlayer: Big blind $bigBlind")
        // TODO ALL IN ...
        gameHand.currentBettingRound.placeBet(smallBlindPlayer, BettingDecision.BettingAction.RAISE, smallBlind)
        gameHand.currentBettingRound.placeBet(bigBlindPlayer, BettingDecision.BettingAction.RAISE, bigBlind)
    }

    private fun applyDecision(gameHand: GameHand, player: Player?, bettingDecision: BettingDecision) {
        val handStrength = handStrengthEvaluator.evaluate(
            player!!.holeCards, gameHand.sharedCards,
            gameHand.table.getActivePlayers().size
        )
        gameHand.applyDecision(player, bettingDecision, gameProperties, handStrength)
        val bettingRound = gameHand.currentBettingRound
        DD++
        logger.log(
            player.toString() + ": " + bettingDecision + " "
                    + bettingRound.getBetForPlayer(player) + "$"
        )
        //logger.log("POTS :"+bettingRound.getPots().toString());
    }

    private fun getWinnersByHandPower(gameHand: GameHand): LinkedHashMap<Int?, List<Player>> {
        val activePlayers: Iterable<Player> = gameHand.table.getActivePlayers()
        val sharedCards: List<Card> = gameHand.sharedCards
        val winners: MutableMap<Int?, MutableList<Player>> = HashMap()
        for (player in activePlayers) {
            val mergeCards: MutableList<Card> = ArrayList(player.holeCards)
            mergeCards.addAll(sharedCards)
            val handPower = handPowerRanker.rank(mergeCards).value
            if (!winners.containsKey(handPower)) {
                winners[handPower] = ArrayList()
            }
            winners[handPower]!!.add(player)
        }
        val sortedWinners = LinkedHashMap<Int?, List<Player>>()
        winners.entries
            .stream()
            .sorted(java.util.Map.Entry.comparingByKey<Int?, List<Player>>())
            .forEachOrdered { (key, value): Map.Entry<Int?, List<Player>> -> sortedWinners[key] = value }
        return sortedWinners
    }

    private fun getWinnersList(gameHand: GameHand): List<Player> {
        val activePlayers: Iterable<Player> = gameHand.table.getActivePlayers()
        val sharedCards: List<Card> = gameHand.sharedCards
        var bestHandPower: HandPower? = null
        val winners: MutableList<Player> = ArrayList()
        for (player in activePlayers) {
            val mergeCards: MutableList<Card> = ArrayList(player.holeCards)
            mergeCards.addAll(sharedCards)
            val handPower = handPowerRanker.rank(mergeCards)
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
     * @param gameHand
     */
    protected fun showDown(gameHand: GameHand) {
        logger.log("--- Showdown")
        calculatePotDistribution(gameHand)

        /*
        // Showdown
        LinkedHashMap<HandPower,List<Player>> winners = getWinners(gameHand);
        winners.entrySet();
        for (Map.Entry<HandPower, List<Player>> handPowerList : winners.entrySet()) {

            // Gains
            int gain = gameHand.getTotalBets() / winners.size();
            int modulo = gameHand.getTotalBets() % winners.size();

            for (Player winner : handPowerList.getValue()) {
                int gainAndModulo = gain;
                if (modulo > 0) {
                    gainAndModulo += modulo;
                }
                winner.addMoney(gainAndModulo);
                modulo--;
            }
        }
        */

        // Opponent modeling
        // opponentModeler.save(gameHand);
    }

    fun calculatePotDistribution(gameHand: GameHand) {

        // Per rank (single or multiple winners), calculate pot distribution.
        val totalPot = gameHand.totalPot
        val potDivision: MutableMap<Player, Int> = HashMap()
        val activePlayers = gameHand.table.getActivePlayers()

        // TODO start from after dealer for odds
        val winnersMap = getWinnersByHandPower(gameHand)
        for ((handPower, winners) in winnersMap) {
            for (pot in gameHand.getPots()) {
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
            //notifyPlayersUpdated(true);
        }
        winnerText.append('.')
        //notifyMessage(winnerText.toString());
        logger.log(winnerText.toString())
    }

    companion object {
        var DD = 0
    }
}