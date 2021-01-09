package fr.o80.slobs

import fr.o80.slobs.model.Performance
import fr.o80.slobs.model.Scene
import fr.o80.slobs.model.Source
import javax.json.JsonObject
import javax.json.JsonValue

internal fun JsonValue.toPerformance(): Performance {
    val jsonObject = this as JsonObject
    return Performance(
        cpu = jsonObject.getJsonNumber("CPU").bigDecimalValue().toFloat(),
        bandwidth = jsonObject.getInt("bandwidth"),
        frameRate = jsonObject.getJsonNumber("frameRate").bigDecimalValue().toFloat(),
        numberDroppedFrames = jsonObject.getInt("numberDroppedFrames"),
        percentageDroppedFrames = jsonObject.getInt("percentageDroppedFrames")
    )
}

internal fun JsonValue.toScene(): Scene {
    val jsonObject = this as JsonObject
    return Scene(
        id = jsonObject.getString("id"),
        resourceId = jsonObject.getString("resourceId"),
        name = jsonObject.getString("name"),
    )
}

internal fun JsonValue.toSource(): Source {
    val jsonObject = this as JsonObject
    return Source(
        resourceId = jsonObject.getString("resourceId"),
        sourceId = jsonObject.getString("sourceId"),
        name = jsonObject.getString("name"),
        muted = jsonObject.getBoolean("muted")
    )
}
