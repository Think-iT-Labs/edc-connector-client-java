import java.net.URL

plugins {
    java
    `java-library`
    alias(libs.plugins.spotless)
   `maven-publish`
   signing
    alias(libs.plugins.nexus)
}

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
    downloadOpenapiSpecTasks.forEach { task -> dependsOn(task) }
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

val downloadOpenapiSpecTasks = listOf(
    registerDownloadOpenapiSpec("Connector", "management"),
    registerDownloadOpenapiSpec("Connector", "observability"),
    registerDownloadOpenapiSpec("FederatedCatalog", "catalog")
)

fun registerDownloadOpenapiSpec(repository: String, context: String): Task {
    val uppercasedContext = context.replaceFirstChar { c -> c.uppercase() }
    val downloadOpenapiSpec by tasks.register("download${uppercasedContext}OpenapiSpec") {
        dependsOn(tasks.findByName("processTestResources"))
        val openapiFile = sourceSets.test.get().output.resourcesDir?.let { File("$it/$context-api.yml") }
        onlyIf { openapiFile?.exists()?.not() ?: false }
        doLast {
            openapiFile?.let { fileSpec ->
                fileSpec.parentFile.mkdirs()
                download("https://eclipse-edc.github.io/$repository/openapi/$context-api/$context-api.yaml")
                    .replace("example: null", "")
                    .let { fileSpec.writeText(it) }
            }
        }
    }
    return downloadOpenapiSpec
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
                developers {
                    developer {
                        id = "think-it"
                        name = "Think-it"
                        email = "sonatype@think-it.io"
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
