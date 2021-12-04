package poklin.model

import poklin.model.cards.CardNumber

class HandPower(
    val handPowerType: HandPowerType,
    val tieBreakingInformation: List<CardNumber?>?
) : Comparable<HandPower?> {
    override fun compareTo(other: HandPower?): Int {
        val typeDifference = (handPowerType.power
                - other!!.handPowerType.power)
        if (typeDifference == 0) {
            for (i in tieBreakingInformation!!.indices) {
                val tieDifference = (tieBreakingInformation[i]!!.power
                        - other.tieBreakingInformation!![i]!!.power)
                if (tieDifference != 0) {
                    return tieDifference
                }
            }
            return 0
        }
        return typeDifference
    }

    val value: Int
        get() {
            val typePower = handPowerType.power
            var tbi = 0
            for (i in tieBreakingInformation!!.indices) {
                tbi += tieBreakingInformation[i]!!.power
            }
            return typePower * 100 + tbi
        }

    override fun toString(): String {
        return (handPowerType.toString() + " "
                + tieBreakingInformation.toString())
    }
}