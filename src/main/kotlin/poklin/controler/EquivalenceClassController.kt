package poklin.controler

import com.google.inject.Inject
import poklin.model.cards.*

class EquivalenceClassController @Inject constructor() {
    val equivalenceClasses: MutableCollection<EquivalenceClass>

    /**
     * Converts two cards into their corrispondent equivalence class
     */
    fun cards2Equivalence(card1: Card, card2: Card): EquivalenceClass {
        val equivalenceClass: EquivalenceClass
        equivalenceClass = if (card1.suit == card2.suit) { // suited
            EquivalenceClassSuited(card1.number, card2.number)
        } else { // unsuited
            EquivalenceClassUnsuited(card1.number, card2.number)
        }
        return equivalenceClass
    }

    fun generateAllEquivalenceClass() {
        var equivalenceClass: EquivalenceClass
        val allCardNumbers: MutableList<CardNumber> = ArrayList()

        //generateThePairs
        for (number in CardNumber.values()) {
            equivalenceClass = EquivalenceClassUnsuited(number, number)
            equivalenceClasses.add(equivalenceClass)
            allCardNumbers.add(number)
        }

        //generate other equivalences 		
        for (i in allCardNumbers.indices) {
            for (j in i + 1 until allCardNumbers.size) {
                equivalenceClass = EquivalenceClassUnsuited(allCardNumbers[i], allCardNumbers[j])
                equivalenceClasses.add(equivalenceClass)
                equivalenceClass = EquivalenceClassSuited(allCardNumbers[i], allCardNumbers[j])
                equivalenceClasses.add(equivalenceClass)
            }
        }
    }

    init {
        equivalenceClasses = ArrayList()
    }
}