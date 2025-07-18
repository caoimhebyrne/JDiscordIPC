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
    implementation(rootProject)
    implementation(project(":java-modern"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_24
    targetCompatibility = JavaVersion.VERSION_24
}
