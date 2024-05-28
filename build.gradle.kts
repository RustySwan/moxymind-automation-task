plugins {
    kotlin("jvm") version "1.9.24"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.microsoft.playwright:playwright:1.44.0")
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

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}