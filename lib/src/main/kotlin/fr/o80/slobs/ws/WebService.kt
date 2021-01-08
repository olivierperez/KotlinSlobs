package fr.o80.slobs.ws

import kotlinx.coroutines.CompletableDeferred
import java.net.URI
import javax.json.JsonValue
import javax.websocket.*

@ClientEndpoint
class WebService(
    private val endpointURI: URI,
    private val token: String
) {

    private lateinit var messenger: Messenger

    private var session: Session? = null

    private var connectionDeferred: CompletableDeferred<Unit>? = null

    fun request(
        resourceId: String,
        methodName: String,
        args: Array<String>? = null,
        callback: (Answer) -> Unit
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
        if (message == "o") {
            messenger.sendText(prepareAuthRequest(token)) { answer ->
                val connected = (answer.result.valueType) == JsonValue.ValueType.TRUE
                if (connected) {
                    connectionDeferred?.complete(Unit)
                } else {
                    connectionDeferred?.completeExceptionally(
                        IllegalStateException("Failed to connect to server: $answer")
                    )
                }
            }
        } else {
            messenger.onMessage(message)
        }
    }
}
