import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform") version "2.1.20"
    id("com.vanniktech.maven.publish") version "0.29.0"
}

group = "io.github.lexa-diky"
version = "0.0.1"

kotlin {
    jvmToolchain(17)

    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    reports {
        junitXml.required = true
    }
}
