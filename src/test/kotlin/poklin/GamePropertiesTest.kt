package poklin

import org.junit.Test
import poklin.controler.player.PlayerControllerBluff

internal class GamePropertiesTest {

    @Test
    fun testNextDealer() {
        val gp = GameProperties(20f, 10f, dealerSeat = 1)
        gp.addPlayer(Player(1, 1000f, PlayerControllerBluff()))
        gp.addPlayer(Player(2, 1000f, PlayerControllerBluff()))
        gp.addPlayer(Player(3, 1000f, PlayerControllerBluff()))
        gp.nextDealer()
        assert(gp.dealerSeat == 2)
        gp.nextDealer()
        assert(gp.dealerSeat == 3)
        gp.nextDealer()
        assert(gp.dealerSeat == 1)
    }
}