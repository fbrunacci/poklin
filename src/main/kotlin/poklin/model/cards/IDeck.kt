package poklin.model.cards

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

abstract class IDeck {
    abstract fun removeTopCard(): Card
    abstract fun removeCard(card: Card): Boolean

    fun clone(): IDeck {
        val byteArrayOutputStream = ByteArrayOutputStream()
        ObjectOutputStream(byteArrayOutputStream).use { outputStream ->
            outputStream.writeObject(this)
        }

        val bytes = byteArrayOutputStream.toByteArray()

        ObjectInputStream(ByteArrayInputStream(bytes)).use { inputStream ->
            return inputStream.readObject() as IDeck
        }
    }
}