package poklin.parser

import poklin.compose.state.TableState
import poklin.model.bet.BettingDecision
import poklin.model.cards.Card
import poklin.model.cards.CardNumber
import poklin.model.cards.CardSuit
import poklin.utils.ConsoleLogger
import poklin.utils.ILogger
import java.util.regex.Pattern

class AbsParser(val tableState: TableState, private val logger: ILogger = ConsoleLogger()) {

    val parseGameHandler = ParseGameHandler(tableState)

    var smallBlind: Float? = null
    var bigBlind: Float? = null
    var dealerSeat: Int? = null

    var playersName: MutableList<String> = ArrayList()
    fun parseLine(line: String) {
        var line = line
        line = line.trim { it <= ' ' }
        line = line.replace(" - ", ": ")
        if (line.length == 0) return
        logger.log("|**|$line|**|")
        if (line.startsWith("Stage ")) {
            parseStage(line)
        } else if (line.startsWith("Table: ")) {
            parseTable(line)
        } else if (line.startsWith("Seat ")) {
            parseSeat(line)
        } else if (line.contains(":") && playersName.contains(extractPlayerName(line))) {
            parsePlayerAction(line)
        } else if (line == "*** HOLE CARDS ***") {
            parseGameHandler.holeCards()
        } else if (line.startsWith("*** FLOP ***")) {
            parseFlop(line)
        }
    }

    private fun parseFlop(line: String) {
        val m = flopPattern.matcher(line)
        if (m.find()) {
            logger.log("flop: " + m.group(1) + "," + m.group(2) + "," + m.group(3))
            parseGameHandler.setFlop(parseCard(m.group(1)), parseCard(m.group(2)), parseCard(m.group(3)))
        }
    }

    private fun parseCard(cardString: String): Card {
        return Card(CardSuit.fromText(cardString), CardNumber.fromText(cardString))
    }

    private fun parsePlayerAction(line: String) {
        val playerName = extractPlayerName(line)
        val action = extractAction(line)
        logger.log("$playerName action:$action")

        // Gzgarry: calls €1
        if (action.startsWith("calls")) {
            parseGameHandler.setNextPlayerAction(playerName, BettingDecision(BettingDecision.BettingAction.CALL))
        } else if (action.startsWith("checks")) {
            parseGameHandler.setNextPlayerAction(playerName, BettingDecision(BettingDecision.BettingAction.CHECK))
        } else if (action.startsWith("Post")) {
            val m = amountPattern.matcher(line)
            if (m.find()) {
                val amount = m.group(1).toFloat()
                if (action.contains("small blind")) {
                    parseGameHandler.setNextPlayerAction(
                        playerName,
                        BettingDecision(BettingDecision.BettingAction.SMALLBLIND, amount = amount)
                    )
                } else {
                    parseGameHandler.setNextPlayerAction(
                        playerName,
                        BettingDecision(BettingDecision.BettingAction.BIGBLIND, amount = amount)
                    )
                }
            }

            //} else if ( action.startsWith("") ) {
            //} else if ( action.startsWith("") ) {
        }
    }

    private fun extractPlayerName(line: String): String {
        return line.substring(0, line.indexOf(":"))
    }

    private fun extractAction(line: String): String {
        return line.substring(line.indexOf(":") + 2)
    }

    private fun parseSeat(line: String) {
        val m = seatPattern.matcher(line)
        if (m.find()) {
            val name = m.group(2)
            val seat = m.group(1).toInt()
            val chips = m.group(3).toFloat()
            playersName.add(name)
            parseGameHandler.addPlayer(name, seat, chips, seat == dealerSeat)
        }
    }

    fun parseStage(line: String?) {
        val m = handPattern.matcher(line)
        if (m.find()) {
            val bb = m.group(1).toFloat()
            bigBlind = bb;
            smallBlind = bb / 2;
        }
    }

    private fun parseTable(line: String) {
        val m = tablePattern.matcher(line)
        if (m.find()) {
            dealerSeat = m.group(1).toInt()
        }
    }

    companion object {
        // *** FLOP *** [Js 5c 2s]
        private val flopPattern = Pattern.compile("\\*\\*\\* FLOP \\*\\*\\* \\[(\\w\\w) (\\w\\w) (\\w\\w)\\]")

        // Seat 2: Gzgarry (€44.25 in chips)"
        private val seatPattern = Pattern.compile("Seat (\\d): (.*) \\([€$](\\d*?\\.?\\d*) in chips\\)")

        // Stage #3017237436: Holdem  No Limit $1 - 2009-07-01 00:00:09 (ET)
        private val handPattern = Pattern.compile("\\$(\\d*?\\.?\\d*)")

        // Table: INDIANA ST (Real Money) Seat #5 is the dealer
        private val tablePattern = Pattern.compile("Table: .* Seat #(\\d*) is the dealer")

        private val amountPattern = Pattern.compile("\\$(\\d*?\\.?\\d*)")
    }
}