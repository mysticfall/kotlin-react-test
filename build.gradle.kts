plugins {
    kotlin("js") version "1.5.31"
}

group = "mysticfall.kotlin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

fun versionOf(name: String, isWrapper: Boolean = true): String {
    val artifact = project.property("version.$name") as String

    return if (isWrapper) {
        val wrapper = project.property("version.wrappers") as String

        "$artifact-$wrapper"
    } else {
        artifact
    }
}

dependencies {
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react:${versionOf("react")}")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:${versionOf("react")}")

    implementation(npm("react-test-renderer", versionOf("react", isWrapper = false)))

    testImplementation(kotlin("test-js"))
}

kotlin {
    js(IR) {
        binaries.executable()

        nodejs {
            testTask {
                useMocha()
            }
        }
    }
}
