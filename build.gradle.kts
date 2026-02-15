plugins {
    id("library-conventions")
}

dependencies {
    api(libs.jspecify)

    implementation(libs.jackson.databind)

    testImplementation(libs.junit.jupiter)
}
