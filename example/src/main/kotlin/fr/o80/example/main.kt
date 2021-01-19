package fr.o80.example

import fr.o80.slobs.SlobsClient
import fr.o80.slobs.AsyncSlobsClient
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach

suspend fun main(vararg args: String) = coroutineScope {
    val slobs: SlobsClient = AsyncSlobsClient(
        args[0],
        args[1].toInt(),
        args[2]
    )

    slobs.connect()

    launch {
        slobs.onSceneSwitched().consumeEach { scene ->
            println("On scene switched: ${scene.name}")
        }
    }

    val activeScene = slobs.getActiveScene()
    val sources = slobs.getSources()
    val scenes = slobs.getScenes()
    val performance = slobs.getPerformance()

    println("============================")
    println("Active scene: ${activeScene.name}")
    println("Sources: ${sources.joinToString { it.name }}")
    println("Scenes: ${scenes.joinToString { it.name }}")
    println("----------------------------")
    println("Performance : $performance")
    println("============================")

    slobs.muteSource(sources[0], true)

    scenes.forEach { scene ->
        println(scene.name)
        slobs.switchTo(scene.id)
        delay(200)
    }


    println("Connected")
}
