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
    implementation(libs.jspecify)
    implementation(rootProject)
}

tasks.test {
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}
