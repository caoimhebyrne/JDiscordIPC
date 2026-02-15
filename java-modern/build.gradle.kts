plugins {
    id("library-conventions")
}

libraryConventions {
    // The Java Unix SocketChannel API became available in Java 16.
    compileTarget = JavaLanguageVersion.of(16)
}

dependencies {
    implementation(rootProject)
}
