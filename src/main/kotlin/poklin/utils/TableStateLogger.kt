package poklin.utils

import poklin.compose.state.TableState

class TableStateLogger : ILogger {
    override fun log(message: String) {
        println(message)
        TableState.log += message + "\n"
    }

    override fun logImportant(message: String) {
        println(message)
        TableState.log += message + "\n"
    }
}