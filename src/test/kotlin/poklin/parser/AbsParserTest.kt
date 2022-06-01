package poklin.parser

import org.junit.Test
import poklin.compose.state.TableState
import java.io.File


class AbsParserTest {

    @Test
    fun testParsePokerStarHandFromFile() {
        val parser = AbsParser(TableState())

        val url = this.javaClass.getResource("aa_sample.txt")
        val file = File(url.toURI())

        val parseGameScanner = ParseGameScanner(file, parser)
        while (parseGameScanner.hasNextLine()) {
            parseGameScanner.scanNextLine()
        }
    }
}