package poklin.persistence

import java.sql.Connection
import java.sql.DriverManager

class PersistenceManager {
    val connection: Connection
    private fun createConnection(): Connection {
        return try {
            Class.forName("org.h2.Driver")
            DriverManager.getConnection("jdbc:h2:data/data", "sa", "")
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException(e.localizedMessage)
        }
    }

    init {
        connection = createConnection()
    }
}