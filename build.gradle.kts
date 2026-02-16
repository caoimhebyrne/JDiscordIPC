plugins {
    id("library-conventions")
}

dependencies {
    api(libs.jspecify)

    implementation(libs.jackson.databind)
    implementation(libs.jackson.module.parameter.names)

    testImplementation(libs.junit.jupiter)
}
