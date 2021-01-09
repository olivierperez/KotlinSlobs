package fr.o80.example

import fr.o80.slobs.SlobClient
import fr.o80.slobs.AsyncSlobsClient
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach

suspend fun main(vararg args: String) = coroutineScope {
    val slobs: SlobClient = AsyncSlobsClient(
        args[0],
        args[1],
        args[2]
    )

    slobs.connect()

    launch {
        slobs.onSceneSwitched().consumeEach { scene ->
            println("On scene switched: ${scene.name}")
        }
    }

    val sources = slobs.getSources()
    val scenes = slobs.getScenes()

    println("============================")
    println("Sources: ${sources.joinToString { it.name }}")
    println("Scenes: ${scenes.joinToString { it.name }}")
    println("============================")

    slobs.muteSource(sources[0], true)

    scenes.forEach { scene ->
        println(scene.name)
        slobs.switchTo(scene)
        delay(200)
    }


    println("Connected")
}
