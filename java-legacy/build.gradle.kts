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
    api(libs.junixsocket.core)

    implementation(libs.jspecify)
    implementation(rootProject)
}

tasks.test {
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
