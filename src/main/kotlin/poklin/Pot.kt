package poklin

import poklin.compose.state.TableState

class Pot {

    private val sidePots: MutableList<SidePot> = ArrayList()
    private val playerContribution: MutableMap<Player, Float> = HashMap()

    fun contributePot(amount: Float, player: Player) {
        player.playerState.moneyPutInPot += amount

        if (!playerContribution.containsKey(player)) {
            playerContribution[player] = amount
        } else {
            playerContribution[player] = playerContribution[player]!!.plus(amount)
        }

        var amount = amount
        for (pot in sidePots) {
            if (!pot.hasContributer(player)) {
                val potBet = pot.bet
                if (amount >= potBet) {
                    // Regular call, bet or raise.
                    pot.addContributer(player)
                    amount -= pot.bet
                } else {
                    // Partial call (all-in); redistribute pots.
                    sidePots.add(pot.split(player, amount))
                    amount = 0f
                }
            }
            if (amount <= 0) {
                break
            }
        }
        if (amount > 0) {
            val sidePot = SidePot(amount)
            sidePot.addContributer(player)
            sidePots.add(sidePot)
        }
    }

    fun getSidePots(): List<SidePot> {
        return sidePots
    }

    fun getPlayerContribution(player: Player): Float {
        return playerContribution[player] ?: 0f
    }

    fun getMaxContribution(): Float {
        return playerContribution.values.maxOrNull() ?: 0f
    }

    val totalPot: Float
        get() {
            var totalPot = 0f
            for (pot in sidePots) {
                totalPot += pot.value
            }
            return totalPot
        }

}