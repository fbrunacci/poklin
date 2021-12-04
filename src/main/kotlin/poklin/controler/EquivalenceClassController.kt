package poklin.controler

import com.google.inject.Inject
import poklin.model.cards.*

class EquivalenceClassController @Inject constructor() {
    val equivalenceClasses: MutableCollection<EquivalenceClass> = ArrayList()

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
}