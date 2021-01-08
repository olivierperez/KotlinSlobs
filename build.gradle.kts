plugins {
    kotlin("jvm") version "1.4.21"
}

group = "fr.o80.slobs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

allprojects {
    repositories {
        mavenCentral()
    }
}
