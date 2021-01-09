package fr.o80.slobs.model

data class Performance(
    val cpu: Float,
    val bandwidth: Int,
    val frameRate: Float,
    val numberDroppedFrames: Int,
    val percentageDroppedFrames: Int
)