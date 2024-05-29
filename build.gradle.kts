plugins {
    kotlin("jvm") version "1.9.24"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.microsoft.playwright:playwright:1.44.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
    implementation("org.apache.commons:commons-lang3:3.12.0")

    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
    systemProperty("headless", System.getProperty("headless") ?: true)
}

tasks.register<Test>("uiTest") {
    useJUnitPlatform {
        includeTags("UI")
    }
    systemProperty("headless", System.getProperty("headless") ?: true)
}

tasks.register<Test>("apiTest") {
    useJUnitPlatform {
        includeTags("API")
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}