plugins {
    kotlin("js") version "1.6.10"

    id("maven-publish")

    signing
}

group = "io.github.mysticfall"
version = "1.2.0-SNAPSHOT"

val isReleasedVersion = !project.version.toString().endsWith("-SNAPSHOT")

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
    js(BOTH) {
        nodejs {
            testTask {
                useMocha()
            }
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("main") {
            from(components["kotlin"])

            if (tasks.names.contains("jsSourcesJar")) {
                artifact(tasks.getByName<Zip>("jsSourcesJar"))
            }

            if (tasks.names.contains("jsIrSourcesJar")) {
                artifact(tasks.getByName<Zip>("jsIrSourcesJar"))
            }

            pom {
                name.set("Kotlin API for React Test Renderer")
                description.set(
                    "Kotlin wrapper for React Test Renderer, which can be used " +
                            "to unit test React components in a Kotlin/JS project."
                )
                url.set("https://github.com/mysticfall/kotlin-react-test")

                licenses {
                    license {
                        name.set("The MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("mysticfall")
                        name.set("Xavier Cho")
                        email.set("mysticfallband@gmail.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/mysticfall/kotlin-react-test.git")
                    developerConnection.set("scm:git:git@github.com:mysticfall/kotlin-react-test.git")
                    url.set("https://github.com/mysticfall/kotlin-react-test")
                }
            }
        }
    }

    repositories {
        maven {
            name = "ossrh"
            url = uri(
                if (isReleasedVersion) {
                    "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                } else {
                    "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                }
            )

            credentials(PasswordCredentials::class)
        }
    }
}

signing {
    setRequired({
        gradle.taskGraph.hasTask("publish")
    })

    useGpgCmd()

    sign(publishing.publications)
}
