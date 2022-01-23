package poklin

import poklin.model.bet.BettingDecision
import poklin.model.bet.BettingDecision.BettingAction
import poklin.model.bet.BettingDecision.BettingAction.*
import poklin.model.bet.BettingDecision.RaiseAmount
import poklin.model.bet.BettingRoundName
import poklin.opponentmodeling.ContextAction
import poklin.opponentmodeling.ContextInformation

class BettingRound(private val game: Game) {
    private val playersBet: MutableMap<Player, Bet>
    val contextInformations: List<ContextInformation> = ArrayList()
    var highestBet = 0
        private set

    fun applyDecision(
        contextInformation: ContextInformation,
        gameProperties: GameProperties
    ) {
        val contextAction = contextInformation.contextAction
        val bettingDecision = contextAction.bettingDecision
        val player = contextAction.player

        // Logger.get().log("Player #"+player.getNumber()+" bettingDecision:"+bettingDecision );
        val playerBet = getPlayerBet(player)
        when (bettingDecision.bettingAction) {
            BettingAction.CHECK -> getPlayerBet(player).action = BettingAction.CHECK
            BettingAction.FOLD -> getPlayerBet(player).action = BettingAction.FOLD
            BettingAction.CALL -> placeBet(
                player,
                bettingDecision.bettingAction,
                Math.min(highestBet - playerBet.amount, player.money)
            )
            BettingAction.RAISE -> when (bettingDecision.raiseAmount) {
                RaiseAmount.RAISE_MIN -> placeBet(
                    player,
                    bettingDecision.bettingAction,
                    Math.min(highestBet + gameProperties.bigBlind, player.money)
                )
                RaiseAmount.RAISE_POT -> placeBet(
                    player,
                    bettingDecision.bettingAction,
                    Math.min(currentPotSize, player.money)
                )
                RaiseAmount.RAISE_ALLIN -> placeBet(player, bettingDecision.bettingAction, player.money)
                RaiseAmount.RAISE_CUSTOM -> placeBet(player, bettingDecision.bettingAction, bettingDecision.amount!!)
            }
        }

        // Don't save context information for pre flop
        // Hand strength is always 0 b/c there's no shared cards
        if (contextAction.bettingRoundName != BettingRoundName.PRE_FLOP) {
            // TODO ? contextInformations.add(contextInformation);
        }
    }

    protected fun getPlayerBet(player: Player): Bet {
        return requireNotNull(playersBet[player])
    }

    protected fun hasPlayerBet(player: Player): Boolean {
        return playersBet.containsKey(player)
    }

    val currentPotSize: Int
        get() {
            var potSize = 0
            for (bet in playersBet.values) {
                potSize = potSize + bet.amount
            }
            return potSize
        }

    fun placeBet(player: Player, action: BettingAction, bet: Int) {
        val previousBet = getPlayerBet(player)
        val previousBetAmount = previousBet!!.amount
        //Logger.get().log("Player #" + player.getNumber() + " placeBet:" + bet);
        require(bet <= player.money) { "You can't bet more money that you own playerBet:" + previousBetAmount + " bet:" + bet + " player:" + player + " money:" + player.money }
        player.removeMoney(bet)
        game.pot.contributePot(bet, player)
        if (bet + previousBetAmount > highestBet) {
            highestBet = bet + previousBetAmount
        } else require(!(bet + previousBetAmount < highestBet && player.money != 0)) { "You can't bet less than the higher bet (bet:" + bet + " highestBet:" + highestBet + ") player:" + player + " money:" + player.money }
        getPlayerBet(player).amount = bet + previousBetAmount
        getPlayerBet(player).action = action
    }

    fun getBetForPlayer(player: Player): Int {
        return if (hasPlayerBet(player)) {
            getPlayerBet(player).amount
        } else 0
    }

    // TODO duplicate code avec pots size ??
    val totalBets: Int
        get() { // TODO duplicate code avec pots size ??
            var totalBets = 0
            for (bet in playersBet.values) {
                totalBets += bet.amount
            }
            return totalBets
        }
    val numberOfRaises: Int
        get() {
            var numberOfRaises = 0
            for (contextInformation in contextInformations) {
                if (contextInformation.contextAction.bettingDecision == BettingDecision.RAISE_MIN) {
                    numberOfRaises++
                }
            }
            return numberOfRaises
        }

    fun getContextActionForPlayer(player: Player): ContextAction? {
        for (i in contextInformations.size downTo 1) {
            val contextInformation = contextInformations[i - 1]
            val contextAction = contextInformation.contextAction
            if (contextAction.player == player) {
                return contextAction
            }
        }
        return null
    }

    /**
     * Fin d'un tour d'enchères (wikipedia)
     * Un tour d'enchères se termine :
     * - soit quand tous les joueurs qui ne se sont pas couchés et qui n'ont pas misé tout leur tapis (donc ceux qui peuvent encore parler) ont parlé et misé le même montant, qu'il soit nul ou non ;
     * - soit quand tous les joueurs, sauf un, se sont couchés.
     *
     * @return
     */
    fun playerCanBet(): Boolean {
        // tous les joueurs sauf 1 se sont couchés
        var playerInGame = 0
        for ((key, value) in playersBet) {
            if (value.action !== BettingAction.FOLD && key.money > 0) {
                playerInGame++
            }
        }
        if (playerInGame == 1) {
            return false
        }

        // chaque joueur doit avoir pris au moins une decision
        for (bet in playersBet.values) {
            if (bet.action == NONE || bet.action == SMALLBLIND || bet.action == BIGBLIND) {
                return true
            }
        }

        // soit quand tous les joueurs qui ne se sont pas couchés et qui n'ont pas misé tout leur tapis
        // (donc ceux qui peuvent encore parler)
        // ont parlé et misé le même montant, qu'il soit nul ou non
        var previousBetAmount: Int? = null
        for ((key, value) in playersBet) {
            if (value.action !== BettingAction.FOLD && key.money > 0) {
                if (previousBetAmount == null) {
                    previousBetAmount = value.amount
                } else {
                    if (previousBetAmount != value.amount) {
                        return true
                    }
                }
            }
        }
        return false
    }

    init {
        // init des Bet pour chaque joueur (action null, amount 0)
        playersBet = HashMap()
        for (player in game.table.getInGamePlayers()) {
            if (player.money > 0) { // TODO voir si > 0 necessaire
                playersBet[player] = Bet(NONE)
            }
        }
    }
}