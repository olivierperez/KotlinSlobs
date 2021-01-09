package fr.o80.slobs

import fr.o80.slobs.model.Performance
import fr.o80.slobs.model.Scene
import fr.o80.slobs.model.Source
import fr.o80.slobs.model.event.SceneSwitched
import kotlinx.coroutines.channels.ReceiveChannel

interface SlobClient {
    suspend fun connect()
    suspend fun getActiveScene(): Scene
    suspend fun getScenes(): List<Scene>
    suspend fun switchTo(scene: Scene)
    suspend fun getSources(): List<Source>
    suspend fun muteSource(source: Source, muted: Boolean)
    suspend fun onSceneSwitched(): ReceiveChannel<SceneSwitched>
    suspend fun getPerformance(): Performance
}
