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
}

group = "dev.caoimhe"
version = "0.1.0"

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
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
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
