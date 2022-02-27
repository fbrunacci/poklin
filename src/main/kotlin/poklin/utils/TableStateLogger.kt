package poklin.utils

import poklin.compose.state.TableState
import javax.inject.Inject

class TableStateLogger @Inject constructor(
    val tableState: TableState
) : ILogger {
    override fun log(message: String) {
        println(message)
        tableState.log += message + "\n"
    }

    override fun logImportant(message: String) {
        println(message)
        tableState.log += message + "\n"
    }
}