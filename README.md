# KotlinSlobs

Kotlin coroutine API for Streamlabs OBS.

## 🎯 Examples

```kotlin
suspend fun main() = coroutineScope {
    val slobs: SlobClient = AsyncSlobsClient(
        "127.0.0.1",
        "59650",
        "<YOUR TOKEN HERE>"
    )

    slobs.connect()

    // Sub examples below ⬇⬇
}
```

### Be notified when active scene changed

```kotlin
launch {
    slobs.onSceneSwitched().consumeEach { scene ->
        println("Scene switched: ${scene.name}")
    }
}
```

### Managed scene

```kotlin
val activeScene = slobs.getActiveScene()
val scenes = slobs.getScenes()

println("Active scene: ${activeScene.name}")
slobs.switchTo(scenes[0])
```

### Manage audio sources

```kotlin
val sources = slobs.getSources()

slobs.muteSource(sources[0], true)
```

### Performance

```kotlin
val performance = slobs.getPerformance()
println(performance.cpu)
println(performance.bandwidth)
```

## 🛒 Import

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.olivierperez.TwitckBot:lib:1.0")
}
```

## 📄 License

See [LICENSE](LICENSE) file for more information.

```
Copyright (C) 2020 Olivier Perez.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
