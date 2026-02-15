plugins {
    java
}

group = "dev.caoimhe.example"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(rootProject)
    implementation(project(":java-legacy"))
    implementation(project(":java-modern"))
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}
