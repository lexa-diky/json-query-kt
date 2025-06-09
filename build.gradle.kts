import io.gitlab.arturbosch.detekt.Detekt

plugins {
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
}

detekt {
    source.from("src")
}

tasks.withType<Test>().configureEach {
    reports {
        junitXml.required = true
    }
}

tasks.withType<Detekt>().configureEach {
    reports {
        xml.required.set(true)
    }
}