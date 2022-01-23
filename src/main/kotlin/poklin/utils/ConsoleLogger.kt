package poklin.utils

class ConsoleLogger : ILogger {
    override fun log(message: String) {
        println(message)
    }

    override fun logImportant(message: String) {
        println(message)
    }
}