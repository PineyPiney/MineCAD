

plugins {
    kotlin("jvm") version "1.8.21"
}

group = "com.pineypiney.minecad"
version = "1.0-SNAPSHOT"

val lwjglVersion = "3.3.2"

// Use https://www.lwjgl.org/customize to set natives
val lwjglNatives = Pair(
    System.getProperty("os.name")!!,
    System.getProperty("os.arch")!!
).let { (name, arch) ->
    when {
        arrayOf("Mac OS X", "Darwin").any { name.startsWith(it) } -> {
            "natives-macos${if (arch.startsWith("aarch64")) "-arm64" else ""}"
        }

        arrayOf("Windows").any { name.startsWith(it) } -> {
            "natives-windows"
        }

        else -> throw Error("Unrecognized or unsupported platform. Please set \"lwjglNatives\" manually")
    }
}

repositories {
    mavenCentral()
    maven("https://raw.githubusercontent.com/kotlin-graphics/mary/master")
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))

    // Logback
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")
    implementation("ch.qos.logback:logback-classic:1.2.11")

    // Game Engine
    implementation("com.github.PineyPiney:GameEngine:c8db49ac75")

    // GLM
    implementation("kotlin.graphics:kool:0.9.75")
    implementation("kotlin.graphics:unsigned:3.3.32")
    implementation("kotlin.graphics:glm:0.9.9.1-11")

    // LWJGL
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-assimp")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-jemalloc")
    implementation("org.lwjgl", "lwjgl-openal")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl-openvr")
    implementation("org.lwjgl", "lwjgl-stb")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-jemalloc", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-openvr", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}