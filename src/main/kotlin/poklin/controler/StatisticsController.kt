package poklin.controler

import poklin.Player

class StatisticsController {
    var player1Wins = 0
        private set

    fun initializeStatistics() {
        player1Wins = 0
    }

    fun storeWinners(winners: List<Player>) {
        for (winner in winners) {
            if (winner.seat == 1) {
                player1Wins++
            }
        }
    }
}