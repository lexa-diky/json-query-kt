import com.vanniktech.maven.publish.SonatypeHost
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin("multiplatform") version "2.1.21"
    kotlin("plugin.serialization") version "2.1.21"
    kotlin("plugin.allopen") version "2.0.21"

    id("com.vanniktech.maven.publish") version "0.32.0"
    id("io.gitlab.arturbosch.detekt") version ("1.23.8")
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.17.0"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.14"
    id("com.star-zero.gradle.githook") version "1.2.1"
}

group = "io.github.lexa-diky"
version = "0.6.0"

val isCiBuild = System.getenv("GITHUB_ACTIONS") != null

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
    if (isCiBuild) {
        js {
            browser()
            nodejs()
        }
        wasmJs {
            browser()
            nodejs()
            d8()
        }
        wasmWasi {
            nodejs()
        }
        linuxArm64()
        iosX64()
        iosArm64()
        iosSimulatorArm64()
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
            }
        }
        named("commonTest") {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        named("jvmTest") {
            dependencies {
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

tasks.withType<Detekt> {
    if (isCiBuild) {
        dependsOn(tasks.named("kotlinStoreYarnLock"))
    }
    setSource(files(project.projectDir))
    buildUponDefaultConfig = true
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

githook {
    failOnMissingHooksDir = true
    createHooksDirIfNotExist = true
    hooks {
        create("pre-commit") {
            task = "check"
        }
    }
}

tasks.named("check") {
    dependsOn("jvmTest", "detektJvmMain", "apiCheck")
}

tasks.register("versionBump") {
    doLast {
        bumpVersionInBuildFile()
        bumpVersionInReadme()
    }
}

fun bumpVersionInBuildFile() {
    val file = File("build.gradle.kts")
    val updatedLines = file.readLines().map { line ->
        val regex = Regex("""version\s*=\s*"(\d+)\.(\d+)\.(\d+)"""")
        val match = regex.find(line)
        if (match != null) {
            val (major, minor, patch) = match.destructured
            val newVersion = "${major}.${minor.toInt() + 1}.$patch"
            line.replace(regex, """version = "$newVersion"""")
        } else {
            line
        }
    }
    file.writeText(updatedLines.joinToString("\n"))
}

fun bumpVersionInReadme() {
    val file = File("README.md")
    val regex = Regex("""(:\d+)\.(\d+)\.(\d+)""")

    val updatedLines = file.readLines().map { line ->
        regex.replace(line) { match ->
            val (major, minor, patch) = match.groupValues.drop(1)
            ":${major.drop(1)}.${minor.toInt() + 1}.$patch"
        }
    }

    file.writeText(updatedLines.joinToString("\n"))
}