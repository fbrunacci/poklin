package poklin.controler

import poklin.GameHand
import poklin.Player
import poklin.opponentmodeling.ContextAction
import poklin.opponentmodeling.ContextAggregate
import poklin.opponentmodeling.ContextInformation
import poklin.opponentmodeling.ModelResult
import poklin.persistence.OpponentsModelPersistence
import javax.inject.Inject

class OpponentModeler @Inject constructor(private val opponentsModelPersistence: OpponentsModelPersistence) {
    private val playerModels: MutableMap<Player, MutableList<ContextAggregate>> = HashMap()
    fun save(gameHand: GameHand) {
        val showdownPlayers = gameHand.table.players
        for (bettingRound in gameHand.getBettingRounds()) {
            for (contextInformation in bettingRound.contextInformations) {
                val player = contextInformation.contextAction.player
                if (showdownPlayers.contains(player)) {
                    // Only save context opponent modeling for players who reach showdown
                    addToPlayerModel(contextInformation)
                }
            }
        }
    }

    fun getEstimatedHandStrength(contextAction: ContextAction): ModelResult? {
        return opponentsModelPersistence.retrieve(contextAction)
    }

    fun getPlayerModels(): Map<Player, MutableList<ContextAggregate>> {
        return playerModels
    }

    private fun addToPlayerModel(contextInformation: ContextInformation) {
        val contextAggregate = getContextAggregate(contextInformation.contextAction)
        contextAggregate.addOccurrence(contextInformation.handStrength)
    }

    private fun getContextAggregate(contextAction: ContextAction): ContextAggregate {
        val player = contextAction.player
        var contextAggregates = playerModels[player]
        if (contextAggregates == null) {
            contextAggregates = ArrayList()
            playerModels[player] = contextAggregates
        }
        for (contextAggregate in contextAggregates) {
            if (contextAggregate.contextAction == contextAction) {
                return contextAggregate
            }
        }
        val contextAggregate = ContextAggregate(contextAction)
        contextAggregates.add(contextAggregate)
        return contextAggregate
    }
}