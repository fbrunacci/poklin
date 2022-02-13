package poklin

import org.junit.Test
import poklin.model.cards.Card
import poklin.model.cards.CardNumber.*
import poklin.model.cards.CardSuit.*
import poklin.model.cards.Cards

internal class HandPowerRankerTest {

    @Test
    fun testRank() {
        println(HandPowerRanker.rank("Ad Kd Qd Jd 10d"))
        println(HandPowerRanker.rank("6s Js As Qs 7s"))
        println(HandPowerRanker.rank("6s Jd As Qs 7s"))
        println(HandPowerRanker.rank("6s Jd 8c Qs 7s"))
    }

}