package poklin.model.cards

abstract class EquivalenceClass(val number1: CardNumber, val number2: CardNumber) {
    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + (number1.hashCode() ?: 0)
        result = prime * result + (number2.hashCode() ?: 0)
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj == null) return false
        if (javaClass != obj.javaClass) return false
        val other = obj as EquivalenceClass
        if (number1 !== other.number1) return false
        return if (number2 !== other.number2) false else number1 === other.number1 && number2 === other.number2
                || number1 === other.number2 && number2 === other.number1
    }

    abstract fun equivalence2cards(): List<Card>
    abstract val type: String
    override fun toString(): String {
        return ("Equivalence Class (" + number1 + ","
                + number2 + ")")
    }
}