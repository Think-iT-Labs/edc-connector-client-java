plugins {
    id("java")
}

group = "io.thinkit"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.assertj)
    testImplementation(libs.junit)
    testImplementation(libs.testcontainers)
}

tasks.test {
    useJUnitPlatform()
}
