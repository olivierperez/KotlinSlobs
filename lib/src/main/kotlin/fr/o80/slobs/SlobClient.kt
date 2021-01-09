package fr.o80.slobs

import fr.o80.slobs.model.Scene
import fr.o80.slobs.model.event.SceneSwitched
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow

interface SlobClient {
    suspend fun connect()
    suspend fun getScenes(): List<Scene>
    suspend fun switchTo(scene: Scene)
    suspend fun onSceneSwitched(): ReceiveChannel<SceneSwitched>
}
