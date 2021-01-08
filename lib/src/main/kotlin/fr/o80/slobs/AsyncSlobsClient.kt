package fr.o80.slobs

import fr.o80.slobs.model.Scene
import fr.o80.slobs.model.event.SceneSwitched
import fr.o80.slobs.ws.WebService
import kotlinx.coroutines.flow.Flow
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
        ws.request("ScenesService", "getScenes") { answer ->
            val scenes = (answer.result as JsonArray).map {
                val jsonObject = it as JsonObject
                Scene(
                    id = jsonObject.getString("id"),
                    resourceId = jsonObject.getString("resourceId"),
                    name = jsonObject.getString("name"),
                )
            }

            continuation.resume(scenes)
        }
    }

    override suspend fun switchTo(scene: Scene) = suspendCoroutine<Unit> { continuation ->
        ws.request("ScenesService", "makeSceneActive", arrayOf(scene.id)) {
            continuation.resume(Unit)
        }
    }

    override suspend fun onSceneSwitched(): Flow<SceneSwitched> {
        TODO("Not yet implemented")
    }
}
