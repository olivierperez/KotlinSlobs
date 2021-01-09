plugins {
    kotlin("jvm")
}

group = "fr.o80.slobs"

dependencies {
    implementation(kotlin("stdlib"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")

    implementation("javax.websocket:javax.websocket-client-api:1.0")
    implementation("javax.json:javax.json-api:1.0")

    implementation("org.glassfish.tyrus:tyrus-core:1.13.1")
    implementation("org.glassfish.tyrus:tyrus-client:1.13.1")
    implementation("org.glassfish.tyrus:tyrus-server:1.13.1")

    implementation("org.glassfish.tyrus:tyrus-websocket-core:1.2.1")
    implementation("org.glassfish.tyrus:tyrus-container-grizzly:1.2.1")

    implementation("org.springframework:spring-websocket:5.2.2.RELEASE")
    implementation("org.springframework:spring-messaging:5.2.2.RELEASE")

    implementation("org.glassfish:javax.json:1.1.4")
    implementation("com.fasterxml.jackson.core:jackson-core:2.4.0")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.4.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.4.0")

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}
