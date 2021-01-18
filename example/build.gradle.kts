plugins {
    maven
    kotlin("jvm") version "1.4.21"
}

group = "fr.o80.example"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":lib"))
}
