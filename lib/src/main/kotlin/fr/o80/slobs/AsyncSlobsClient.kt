package fr.o80.slobs

import fr.o80.slobs.model.Performance
import fr.o80.slobs.model.Scene
import fr.o80.slobs.model.Source
import fr.o80.slobs.model.event.SceneSwitched
import fr.o80.slobs.ws.WebService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import java.net.URI
import javax.json.JsonArray
import javax.json.JsonObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AsyncSlobsClient(
    address: String,
    port: String,
    token: String
) : SlobClient {

    private val ws = WebService(
        URI.create("ws://$address:$port/api/123/abcdefgh/websocket"),
        token
    )

    override suspend fun connect() {
        ws.connect()
    }

    override suspend fun getScenes(): List<Scene> = suspendCoroutine { continuation ->
        ws.request("ScenesService", "getScenes") { result ->
            val scenes = (result as JsonArray).map { it.toScene() }
            continuation.resume(scenes)
        }
    }

    override suspend fun getSources(): List<Source> = suspendCoroutine { continuation ->
        ws.request("AudioService", "getSourcesForCurrentScene") { result ->
            val sources = (result as JsonArray).map { it!!.toSource() }
            continuation.resume(sources)
        }
    }

    override suspend fun muteSource(source: Source, muted: Boolean) = suspendCoroutine<Unit> { continuation ->
        ws.request("SourcesService", "setMuted", arrayOf(source.sourceId, muted)) {
            continuation.resume(Unit)
        }
    }

    override suspend fun getActiveScene(): Scene = suspendCoroutine { continuation ->
        ws.request("ScenesService", "activeScene") {
            continuation.resume(it!!.toScene())
        }
    }

    override suspend fun getPerformance(): Performance = suspendCoroutine { continuation ->
        ws.request("PerformanceService", "getModel") {
            continuation.resume(it!!.toPerformance())
        }
    }

    override suspend fun switchTo(scene: Scene) = suspendCoroutine<Unit> { continuation ->
        ws.request("ScenesService", "makeSceneActive", arrayOf(scene.id)) {
            continuation.resume(Unit)
        }
    }

    override suspend fun onSceneSwitched(): ReceiveChannel<SceneSwitched> {
        val channel = Channel<SceneSwitched>()

        ws.listen("EVENT", "ScenesService.sceneSwitched") { data ->
            val event = SceneSwitched(data.getString("id"), data.getString("name"))
            channel.send(event)
        }

        ws.request("ScenesService", "sceneSwitched") {
            println("Subscribed: ${(it as JsonObject).getString("resourceId")}")
        }

        return channel
    }
}
