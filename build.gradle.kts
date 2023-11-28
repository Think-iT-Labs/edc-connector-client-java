import java.net.URL

plugins {
    java
    `java-library`
    alias(libs.plugins.spotless)
   `maven-publish`
   signing
    alias(libs.plugins.nexus)
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
            from(components["java"])

            pom {
                name.set(project.name)
                description.set("SDK client library for interacting with EDC connector")
                url.set("https://github.com/Think-iT-Labs/edc-connector-client-java")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                scm {
                    url.set("https://github.com/Think-iT-Labs/edc-connector-client-java")
                    connection.set("scm:git:git@github.com:Think-iT-Labs/edc-connector-client-java.git")
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}

nexusPublishing {
    repositories.create("sonatype") {
        nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
        snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
    }
}
