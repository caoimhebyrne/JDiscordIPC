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
    implementation("org.jspecify:jspecify:1.0.0")
    implementation(rootProject)
}

tasks.test {
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}
