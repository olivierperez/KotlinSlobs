package fr.o80.slobs.ws

import javax.json.JsonObject

data class Listener(
    val type: String,
    val resourceId: String,
    val callback: suspend (JsonObject) -> Unit
)
