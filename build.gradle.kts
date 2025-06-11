plugins {
    kotlin("jvm") version "2.1.20"
}

group = "io.github.lexa-diky"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    reports {
        junitXml.required = true
    }
}
