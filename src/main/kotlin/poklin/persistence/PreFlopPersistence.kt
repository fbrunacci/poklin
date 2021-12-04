package poklin.persistence

import poklin.model.cards.EquivalenceClass
import poklin.utils.ILogger
import java.sql.SQLException
import javax.inject.Inject

class PreFlopPersistence @Inject constructor(
    private val persistenceManager: PersistenceManager,
    private val logger: ILogger
) {
    fun persist(numberOfPlayers: Int, equivalenceClass: EquivalenceClass, percentage: Double) {
        try {
            val insert = "INSERT INTO " + TABLE_EQUIVALENCE_NAME + " VALUES(?,?,?,?,?)"
            val statement = persistenceManager.connection.prepareStatement(insert)
            statement.setInt(1, numberOfPlayers)
            statement.setString(2, equivalenceClass.number1.toString())
            statement.setString(3, equivalenceClass.number2.toString())
            statement.setString(4, equivalenceClass.type)
            statement.setDouble(5, percentage)
            statement.executeUpdate()
            statement.close()
        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException(e.localizedMessage)
        }
    }

    fun retrieve(numberOfPlayers: Int, equivalenceClass: EquivalenceClass): Double {
        val number1 = equivalenceClass.number1.toString()
        val number2 = equivalenceClass.number2.toString()
        val type = equivalenceClass.type
        val query = "SELECT wins FROM " + TABLE_EQUIVALENCE_NAME + " WHERE players = ? AND type = ? AND " +
                "((number1 = ? AND number2 = ?) OR (number1 = ? AND number2 = ?))"
        return try {
            val statement = persistenceManager.connection.prepareStatement(query)
            statement.setInt(1, numberOfPlayers)
            statement.setString(2, type)
            statement.setString(3, number1)
            statement.setString(4, number2)
            statement.setString(5, number2)
            statement.setString(6, number1)
            val result = statement.executeQuery()
            if (result.next()) {
                result.getDouble("wins")
            } else {
                throw RuntimeException("Probability not calculated for these parameters")
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException(e.localizedMessage)
        }
    }

    fun print() {
        val query = "SELECT * FROM " + TABLE_EQUIVALENCE_NAME
        try {
            val statement = persistenceManager.connection.prepareStatement(query)
            val result = statement.executeQuery()
            while (result.next()) {
                logger.log(
                    result.getInt("players").toString() + "\t" + result.getString("number1")
                            + "\t" + result.getString("number2") + "\t" + result.getString("type") + "\t"
                            + result.getDouble("wins")
                )
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException(e.localizedMessage)
        }
    }

    private fun init() {
        try {
            val statement = persistenceManager.connection.createStatement()
            val query = "CREATE TABLE IF NOT EXISTS " + TABLE_EQUIVALENCE_NAME + "(players integer," +
                    "number1 VARCHAR_IGNORECASE,number2 VARCHAR_IGNORECASE, type VARCHAR_IGNORECASE, wins double)"
            statement.executeUpdate(query)
        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException(e.localizedMessage)
        }
    }

    companion object {
        private const val TABLE_EQUIVALENCE_NAME = "Equivalences"
    }

    init {
        init()
    }
}