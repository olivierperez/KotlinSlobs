package fr.o80.example

import fr.o80.slobs.SlobClient
import fr.o80.slobs.AsyncSlobsClient
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay


suspend fun main(vararg args: String) = coroutineScope {
    val slobs: SlobClient = AsyncSlobsClient(
        args[0],
        args[1],
        args[2]
    )

    slobs.connect()
    val scenes = slobs.getScenes()
    println("Scenes: ${scenes.joinToString { it.name }}")

    scenes.forEach { scene ->
        slobs.switchTo(scene)
        delay(500)
    }

    println("Connected")
}
