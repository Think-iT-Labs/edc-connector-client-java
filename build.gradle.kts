import org.gradle.nativeplatform.internal.configure.NativeBinaryRules
import java.net.URL

plugins {
    java
    `java-library`
    alias(libs.plugins.spotless)
   `maven-publish`
   signing
}

group = "io.think-it"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenLocal()
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
    val managementApi = sourceSets.test.get().output.resourcesDir?.let { File("$it/management-api.yml") }
    onlyIf { managementApi?.exists()?.not() ?: false }
    doLast {
        managementApi?.let { fileSpec ->
            fileSpec.parentFile.mkdirs()
            download("https://api.swaggerhub.com/apis/eclipse-edc-bot/management-api/${libs.versions.edc.get()}/yaml")
                .replace("example: null", "")
                .let { fileSpec.writeText(it) }
        }
    }
}

fun download(url: String): String = URL(url).openConnection().getInputStream().bufferedReader().use { it.readText() }

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = project.group.toString()
            version = project.version.toString()
            artifactId = project.name
            description = "SDK client library for interacting with EDC connector"
            from(components["java"])
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}
