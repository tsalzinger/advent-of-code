import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm") version "1.9.23"
    `java-library`
    id("org.jlleitschuh.gradle.ktlint") version "14.0.1"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
}

group = "me.salzinger"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

detekt {
    buildUponDefaultConfig = true
    config.from("$rootDir/detekt-override,.yml")
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        freeCompilerArgs =
            listOf(
                "-Xallow-reified-type-in-catch",
                "-Xallow-contracts-on-more-functions",
                "-Xallow-condition-implies-returns-contracts",
                "-Xallow-holdsin-contract",
                "-Xcontext-parameters",
                "-Xdata-flow-based-exhaustiveness",
                "-Xjsr305=strict",
            )

        optIn =
            listOf(
                "kotlin.contracts.ExperimentalContracts",
                "kotlin.contracts.ExperimentalExtendedContracts",
            )
    }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.1")
    testImplementation("org.assertj:assertj-core:3.27.6")
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            events(
                TestLogEvent.PASSED,
                TestLogEvent.SKIPPED,
                TestLogEvent.FAILED,
            )
        }
    }
}
