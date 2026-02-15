plugins {
    java
    `java-library`
}

group = "dev.caoimhe"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    api("org.jspecify:jspecify:1.0.0")
    implementation("tools.jackson.core:jackson-databind:3.0.4")

    testImplementation(platform("org.junit:junit-bom:6.0.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

// The project must be compiled with Java 8 compatibility.
tasks.withType<JavaCompile>().configureEach {
    options.release = 8
}

// Tests can be compiled targeting the JVM version being used by Gradle.
tasks.named<JavaCompile>("compileTestJava") {
    options.release = java.toolchain.languageVersion.get().asInt()
}

tasks.test {
    useJUnitPlatform()
}
