plugins {
    kotlin("jvm")
}

group = "fr.o80.example"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":lib"))
}
