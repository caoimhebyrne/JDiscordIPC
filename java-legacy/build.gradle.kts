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
    api("com.kohlschutter.junixsocket:junixsocket-core:2.10.1")
    implementation("org.jspecify:jspecify:1.0.0")
    implementation(rootProject)
}

tasks.test {
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
