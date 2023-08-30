plugins {
    id("java")
    `java-library`
}

group = "io.thinkit"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(libs.jakarta.json.api)

    implementation(libs.titanium.json.ld)
    implementation(libs.jakarta.json)
    implementation(libs.parsson)
    implementation(libs.jackson.databind)

    testImplementation(libs.assertj)
    testImplementation(libs.junit)
    testImplementation(libs.testcontainers)
}

tasks.test {
    useJUnitPlatform()
}
