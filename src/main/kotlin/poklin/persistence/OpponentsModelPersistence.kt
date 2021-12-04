package poklin.persistence

import poklin.opponentmodeling.ContextAction
import poklin.opponentmodeling.ContextAggregate
import poklin.opponentmodeling.ModelResult
import poklin.utils.ILogger
import java.sql.SQLException
import java.text.DecimalFormat
import javax.inject.Inject

class OpponentsModelPersistence @Inject constructor(
    private val persistenceManager: PersistenceManager,
    private val logger: ILogger
) {
    fun persist(contextAggregate: ContextAggregate) {
        try {
            val insert = "INSERT INTO " + TABLE_OPPONENTS_MODEL + " VALUES(?,?,?,?,?,?,?,?,?)"
            val statement = persistenceManager.connection.prepareStatement(insert)
            val contextAction = contextAggregate.contextAction
            statement.setInt(1, contextAction.player.seat)
            statement.setString(2, contextAction.bettingDecision.toString())
            statement.setString(3, contextAction.bettingRoundName.toString())
            //            statement.setString(4, contextAction.getContextRaises().toString());
//            statement.setString(5, contextAction.getContextPlayers().toString());
//            statement.setString(6, contextAction.getContextPotOdds().toString());
            statement.setInt(7, contextAggregate.numberOfOccurrences)
            statement.setDouble(8, contextAggregate.handStrengthAverage)
            statement.setDouble(9, contextAggregate.deviation)
            statement.executeUpdate()
            statement.close()
        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException(e.localizedMessage)
        }
    }

    fun retrieve(contextAction: ContextAction): ModelResult {
        val query = "SELECT * FROM " + TABLE_OPPONENTS_MODEL + " WHERE player = ? AND decision = ? AND " +
                "roundname = ? AND raises = ? AND playercount = ? AND potodds = ?"
        return try {
            val statement = persistenceManager.connection.prepareStatement(query)
            statement.setInt(1, contextAction.player.seat)
            statement.setString(2, contextAction.bettingDecision.toString())
            statement.setString(3, contextAction.bettingRoundName.toString())
            //            statement.setString(4, contextAction.getContextRaises().toString());
//            statement.setString(5, contextAction.getContextPlayers().toString());
//            statement.setString(6, contextAction.getContextPotOdds().toString());
            val result = statement.executeQuery()
            if (result.next()) {
                ModelResult(
                    result.getDouble("handstrength"), result.getDouble("deviation"),
                    result.getInt("occurences")
                )
            } else {
                ModelResult(0.0, 0.0, 0)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException(e.localizedMessage)
        }
    }

    fun print() {
        val query = "SELECT * FROM " + TABLE_OPPONENTS_MODEL + " ORDER BY player, decision, roundname, " +
                "raises, playercount, potodds"
        try {
            val statement = persistenceManager.connection.prepareStatement(query)
            val result = statement.executeQuery()
            logger.log("P\tDecision\tRound\tRaises\t#Players\tPO\tOccurences\tHS\tDeviation")
            val f = DecimalFormat("##.0000")
            while (result.next()) {
                val handstrength = f.format(result.getDouble("handstrength"))
                val deviation = f.format(result.getDouble("deviation"))
                logger.log(
                    result.getInt("player")
                        .toString() + "\t" + result.getString("decision") + "\t" + result.getString("roundname") + "\t" + result.getString(
                        "raises"
                    ) + "\t" + result.getString("playercount") +
                            "\t" + result.getString("potodds") + "\t" + result.getString("occurences") + "\t" +
                            handstrength + "\t" + deviation
                )
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException(e.localizedMessage)
        }
    }

    fun clear() {
        val query = "TRUNCATE TABLE " + TABLE_OPPONENTS_MODEL
        try {
            val statement = persistenceManager.connection.createStatement()
            statement.execute(query)
        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException(e.localizedMessage)
        }
    }

    private fun init() {
        try {
            val statement = persistenceManager.connection.createStatement()
            //statement.execute("DROP TABLE "+TABLE_OPPONENTS_MODEL);
            val query = "CREATE TABLE IF NOT EXISTS " + TABLE_OPPONENTS_MODEL + "(player integer," +
                    "decision VARCHAR_IGNORECASE,roundname VARCHAR_IGNORECASE, raises VARCHAR_IGNORECASE, " +
                    "playercount VARCHAR_IGNORECASE, potodds VARCHAR_IGNORECASE, occurences integer, " +
                    "handstrength double, deviation double)"
            statement.executeUpdate(query)
        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException(e.localizedMessage)
        }
    }

    companion object {
        private const val TABLE_OPPONENTS_MODEL = "Opponents"
    }

    init {
        init()
    }
}