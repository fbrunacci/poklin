package poklin

class Pot(var bet: Int) {

    val contributors: MutableSet<Player> = HashSet()

    fun addContributer(player: Player) {
        contributors.add(player)
    }

    fun hasContributer(player: Player): Boolean {
        return contributors.contains(player)
    }

    val potSize: Int
        get() = bet * contributors.size

    fun split(player: Player, partialBet: Int): Pot {
        val pot = Pot(bet - partialBet)
        for (contributer in contributors) {
            pot.addContributer(contributer)
        }
        bet = partialBet
        contributors.add(player)
        return pot
    }

    fun clear() {
        bet = 0
        contributors.clear()
    }

    val value: Int
        get() = bet * contributors.size

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(bet.toString())
        sb.append(": {")
        var isFirst = true
        for (contributer in contributors) {
            if (isFirst) {
                isFirst = false
            } else {
                sb.append(", ")
            }
            sb.append(contributer.seat)
        }
        sb.append('}')
        sb.append(" (Total: ")
        sb.append(potSize.toString())
        sb.append(')')
        return sb.toString()
    }
}