package poklin.opponentmodeling

class ContextAggregate(val contextAction: ContextAction) {
    var handStrengths: MutableList<Double> = ArrayList()
    fun addOccurrence(handStrength: Double) {
        handStrengths.add(handStrength)
    }

    val handStrengthAverage: Double
        get() {
            var sum = 0.0
            for (handStrength in handStrengths) {
                sum += handStrength
            }
            return sum / numberOfOccurrences
        }
    val deviation: Double
        get() {
            val avg = handStrengthAverage
            var variance = 0.0
            for (handStrength in handStrengths) {
                variance += Math.pow(handStrength - avg, 2.0)
            }
            return Math.sqrt(variance / numberOfOccurrences)
        }
    val numberOfOccurrences: Int
        get() = handStrengths.size
}