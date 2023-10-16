import java.net.URL

plugins {
    id("java")
    `java-library`
    alias(libs.plugins.spotless)
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
    dependsOn(downloadOpenapiSpec)
}

spotless {
    java {
        palantirJavaFormat()
    }
}

allprojects {
    afterEvaluate {
        val spotless = tasks.findByName("spotlessApply")
        if (spotless != null) {
            tasks.compileJava {
                finalizedBy(spotless)
            }
        }
    }
}

val downloadOpenapiSpec by tasks.register("downloadOpenapiSpec") {
    dependsOn(tasks.findByName("processTestResources"))
    doLast {
        sourceSets.test.get().output.resourcesDir?.let { resourcesFolder ->
            resourcesFolder.mkdirs()
            download("https://api.swaggerhub.com/apis/eclipse-edc-bot/management-api/${libs.versions.edc.get()}/yaml")
                .replace("example: null", "")
                .let { File("$resourcesFolder/management-api.yml").writeText(it) }
        }
    }
}

fun download(url: String): String = URL(url).openConnection().getInputStream().bufferedReader().use { it.readText() }
