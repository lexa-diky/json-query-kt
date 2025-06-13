import com.vanniktech.maven.publish.SonatypeHost
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin("multiplatform") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20"
    kotlin("plugin.allopen") version "2.0.20"

    id("com.vanniktech.maven.publish") version "0.29.0"
    id("io.gitlab.arturbosch.detekt") version ("1.23.8")
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.17.0"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.14"
    id("com.star-zero.gradle.githook")version "1.2.1"
}

group = "io.github.lexa-diky"
version = "0.4.0"

kotlin {
    jvmToolchain(17)

    jvm {
        compilations.create("benchmark") {
            associateWith(this@jvm.compilations.getByName("main"))
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.14")
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("com.lemonappdev:konsist:0.17.3")
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

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
    coordinates(
        groupId = group.toString(),
        artifactId = "json-query",
        version = version.toString()
    )

    pom {
        name = "json-query-kt"
        description = "Simple and powerful JSON query library for Kotlin"
        inceptionYear = "2025"
        url = "https://github.com/lexa-diky/json-query-kt"

        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/license/mit/"
                distribution = "repo"
            }
        }
        developers {
            developer {
                id = "lexa-diky"
                name = "Aleksei Iakovlev"
                url = "https://github.com/lexa-diky"
            }
        }
        scm {
            url = "https://github.com/lexa-diky/json-query-kt"
        }
    }
}

tasks.withType<Detekt>{
    setSource(files(project.projectDir))
    exclude("**/*.kts")
    exclude("**/build/**")
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}

benchmark {
    targets {
        register("jvmBenchmark")
    }
}

val check by tasks.getting {
    dependsOn("jvmTest", "detektJvmMain", "apiCheck")
}

githook {
    failOnMissingHooksDir = true
    createHooksDirIfNotExist = true
    hooks {
        create("pre-commit") {
            task = "check"
        }
    }
}