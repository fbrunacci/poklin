package poklin.parser

import org.junit.Test
import poklin.compose.state.TableState
import java.io.File
import java.nio.file.Files

class AbsParserTest {

    @Test
    fun testParsePokerStarHandFromFile() {
        val parser = AbsParser(TableState())
        val url = this.javaClass.getResource("aa_sample.txt")
        val f = File(url.toURI())
        Files.lines(f.toPath()).use { linesStream ->
            linesStream.forEach { line: Any? ->
                parser.parseLine(
                    (line as String?)!!
                )
            }
        }
    }
}