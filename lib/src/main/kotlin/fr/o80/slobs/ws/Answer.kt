package fr.o80.slobs.ws

import javax.json.JsonValue

data class Answer(
    val jsonrpc: String,
    val id: Int,
    val result: JsonValue
)
