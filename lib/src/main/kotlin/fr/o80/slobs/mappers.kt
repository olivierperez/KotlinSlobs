package fr.o80.slobs

import fr.o80.slobs.model.Scene
import javax.json.JsonObject
import javax.json.JsonValue

internal fun JsonValue.toScene(): Scene {
    val jsonObject = this as JsonObject
    return Scene(
        id = jsonObject.getString("id"),
        resourceId = jsonObject.getString("resourceId"),
        name = jsonObject.getString("name"),
    )
}
