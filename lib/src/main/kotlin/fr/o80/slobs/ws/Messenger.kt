package fr.o80.slobs.ws

import org.glassfish.json.JsonProviderImpl
import java.util.concurrent.atomic.AtomicInteger
import javax.json.JsonReader
import javax.websocket.Session

typealias AnswerCallback = (Answer) -> Unit

class Messenger(private val session: Session) {

    private val nextId = AtomicInteger(0)

    private val callbacks = hashMapOf<Int, AnswerCallback>()

    fun sendText(text: String, callback: AnswerCallback) {
        val id = nextId.getAndIncrement()
        val toSend = text.replace("#ID#", id.toString())
        callbacks[id] = callback
        session.asyncRemote.sendText(toSend)
    }

    fun onMessage(message: String) {
        if (message.startsWith('a')) {
            val answer = parseAnswer(message)
            callbacks[answer.id]?.let { callback ->
                callback(answer)
                callbacks.remove(answer.id)
            }
        }
    }

    private fun parseAnswer(message: String): Answer {
        val unwrapped = message
            .substring(3, message.length - 4)
            .replace("\\\"", "\"")
            .replace("\\\\\"", "\\\"")

        return JsonProviderImpl.provider()
            .createReader(unwrapped.byteInputStream())
            .use { jsonReader: JsonReader ->
                val response = jsonReader.readObject()
                val jsonrpc = response.getString("jsonrpc")
                val id = response.getString("id").toInt()
                val result = response["result"]!!

                Answer(jsonrpc, id, result)
            }
    }

}
