package fr.o80.slobs.ws

import java.util.concurrent.atomic.AtomicInteger
import javax.json.JsonValue
import javax.websocket.Session

typealias AnswerCallback = (JsonValue?) -> Unit

class Messenger(private val session: Session) {

    private val nextId = AtomicInteger(0)

    private val callbacks = hashMapOf<Int, AnswerCallback>()

    fun sendText(text: String, callback: AnswerCallback) {
        val id = nextId.getAndIncrement()
        val toSend = text.replace("#ID#", id.toString())
        callbacks[id] = callback
        session.asyncRemote.sendText(toSend)
    }

    fun onMessage(id: Int, result: JsonValue?) {
        callbacks[id]?.let { callback ->
            callback(result)
            callbacks.remove(id)
        }
    }

}
