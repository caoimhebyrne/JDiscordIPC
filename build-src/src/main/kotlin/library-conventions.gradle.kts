// https://docs.gradle.org/current/userguide/implementing_gradle_plugins_precompiled.html#sec:plugin_conventions
interface LibraryConventionsPluginExtension {
    /**
     * The [JavaLanguageVersion] to target when compiling.
     */
    val compileTarget: Property<JavaLanguageVersion>
}

val extension = project.extensions.create<LibraryConventionsPluginExtension>("libraryConventions")
extension.compileTarget.convention(JavaLanguageVersion.of(8))

plugins {
    java
    `java-library`
    `maven-publish`
}

group = "dev.caoimhe"
version = "0.1.0-SNAPSHOT"

// If this is the root project, then we don't need to add a classifier to the archives name.
base.archivesName =  if (project == rootProject) {
    "jdiscordipc"
} else {
    "jdiscordipc-" + project.name
}

repositories {
    mavenCentral()
}

java {
    withSourcesJar()
    withJavadocJar()

    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

val mavenPublishingUsernameProvider = providers.environmentVariable("MAVEN_PUBLISHING_USERNAME")
val mavenPublishingPasswordProvider = providers.environmentVariable("MAVEN_PUBLISHING_PASSWORD")

publishing {
    repositories {
        maven {
            name = "maven-central-snapshots"
            url = uri("https://central.sonatype.com/repository/maven-snapshots")

            val mavenPublishingUsername = mavenPublishingUsernameProvider.orNull
            val mavenPublishingPassword = mavenPublishingPasswordProvider.orNull

            if (mavenPublishingUsername != null && mavenPublishingPassword != null) {
                credentials {
                    username = mavenPublishingUsername
                    password = mavenPublishingPassword
                }
            } else {
                println("Not configuring maven publishing credentials as MAVEN_PUBLISHING environment variables were not set!")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            artifactId = base.archivesName.get()

            from(components["java"])

            pom {
                name = "JDiscordIPC"
                description = "A Java library for interacting with Discord via IPC (a.k.a. RPC, Game SDK, Social SDK, etc.)"
                url = "https://github.com/caoimhebyrne/JDiscordIPC"

                licenses {
                    license {
                        name = "MIT"
                        url = "https://opensource.org/licenses/MIT"
                    }
                }
            }
        }
    }
}

// The project must be compiled with whatever compatibility was requested by the project. This is Java 8 by default.
tasks.withType<JavaCompile>().configureEach {
    options.release = extension.compileTarget.get().asInt()
}

// Tests can be compiled targeting the JVM version being used by Gradle.
tasks.named<JavaCompile>("compileTestJava") {
    options.release = java.toolchain.languageVersion.get().asInt()
}

tasks.test {
    useJUnitPlatform()
}
