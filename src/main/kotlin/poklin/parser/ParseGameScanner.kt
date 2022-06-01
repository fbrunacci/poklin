package poklin.parser

import java.io.File
import java.util.*

class ParseGameScanner(file: File, private val parser: AbsParser) {

    private val sc = Scanner(file)

    fun scanNextLine() {
        if (sc.hasNextLine()) {
            parser.parseLine(sc.nextLine())
        }
    }

    fun hasNextLine(): Boolean {
        return sc != null && sc.hasNextLine();
    }
}