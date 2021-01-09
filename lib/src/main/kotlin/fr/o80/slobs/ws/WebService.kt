package fr.o80.slobs.ws

import kotlinx.coroutines.*
import org.glassfish.json.JsonProviderImpl
import java.net.URI
import javax.json.JsonObject
import javax.json.JsonReader
import javax.json.JsonValue
import javax.websocket.*

@ClientEndpoint
class WebService(
    private val endpointURI: URI,
    private val token: String
) {

    private lateinit var messenger: Messenger

    private val listeners: MutableList<Listener> = mutableListOf()

    private var session: Session? = null

    private var connectionDeferred: CompletableDeferred<Unit>? = null

    private val scope = CoroutineScope(Dispatchers.IO)

    fun request(
        resourceId: String,
        methodName: String,
        args: Array<String>? = null,
        callback: (JsonValue) -> Unit
    ) {
        val argsStr = args?.let {
            """, \"args\":${args.joinToString("\\\",\\\"", prefix = "[\\\"", postfix = "\\\"]")}"""
        } ?: ""

        val payload =
            """["{\"jsonrpc\": \"2.0\", \"id\": \"#ID#\", \"method\": \"$methodName\", \"params\": {\"resource\": \"$resourceId\" $argsStr}, \"compactMode\": true}"]"""

        messenger.sendText(payload, callback)
    }

    suspend fun connect() {
        connectionDeferred = CompletableDeferred()
        val container: WebSocketContainer = ContainerProvider.getWebSocketContainer()
        container.connectToServer(this, endpointURI)
        connectionDeferred?.await()
    }

    @OnOpen
    fun onOpen(session: Session) {
        this.session = session
        this.messenger = Messenger(session)
    }

    @OnClose
    fun onClose(session: Session, reason: CloseReason) {
        this.session = null
    }

    @OnError
    fun onError(t: Throwable) {
    }

    @OnMessage
    fun onMessage(message: String, session: Session) {
        scope.launch {
            if (message == "o") {
                requestAuthentication()
            } else if (message.startsWith('a')) {
                onAnswer(message)
            }
        }

    }

    fun listen(type: String, resourceId: String, callback: suspend (JsonObject) -> Unit) {
        listeners += Listener(type, resourceId, callback)
    }

    private fun requestAuthentication() {
        messenger.sendText(prepareAuthRequest(token)) { result ->
            val connected = (result.valueType) == JsonValue.ValueType.TRUE
            if (connected) {
                connectionDeferred?.complete(Unit)
            } else {
                connectionDeferred?.completeExceptionally(
                    IllegalStateException("Failed to connect to server: $result")
                )
            }
        }
    }

    private suspend fun onAnswer(message: String) {
        val unwrapped = message
            .substring(3, message.length - 4)
            .replace("\\\"", "\"")
            .replace("\\\\\"", "\\\"")

        val answer = JsonProviderImpl.provider()
            .createReader(unwrapped.byteInputStream())
            .use { jsonReader: JsonReader ->
                val response = jsonReader.readObject()
                val jsonrpc = response.getString("jsonrpc")
                val id = if (response.isNull("id")) null else response.getString("id")?.toInt()
                val result = response["result"]!!

                Answer(jsonrpc, id, result)
            }

        if (answer.id != null) {
            messenger.onMessage(answer.id, answer.result)
        } else {
            val result = answer.result as JsonObject
            val type = result.getString("_type")
            val resourceId = result.getString("resourceId")
            listeners.filter { listener -> listener.type == type && listener.resourceId == resourceId }
                .forEach { listener -> listener.callback(result.getJsonObject("data")) }
        }
    }
}
