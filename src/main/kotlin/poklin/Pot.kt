package poklin

import poklin.compose.state.TableState

class Pot {

    private val sidePots: MutableList<SidePot> = ArrayList()
    private val playerContribution: MutableMap<Player, Int> = HashMap()

    fun contributePot(amount: Int, player: Player) {
        TableState.pot += amount
        TableState.playerStateAtSeat(player.seat)?.moneyPutInPot += amount

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
                    amount = 0
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

    fun getPlayerContribution(player: Player): Int {
        return playerContribution[player] ?: 0
    }

    fun getMaxContribution(): Int {
        return playerContribution.values.maxOrNull() ?: 0
    }

    val totalPot: Int
        get() {
            var totalPot = 0
            for (pot in sidePots) {
                totalPot += pot.value
            }
            return totalPot
        }

}