import com.vanniktech.maven.publish.SonatypeHost

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
