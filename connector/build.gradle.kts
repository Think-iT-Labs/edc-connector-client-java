plugins {
    `java-library`
    alias(libs.plugins.shadow)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.edc.controlplane.base.bom)
    implementation(libs.edc.dataplane.base.bom)
    implementation(libs.edc.federatedcatalog.bom)
    implementation(libs.edc.crawler.spi)
    runtimeOnly(libs.edc.iam.mock)
}

tasks.shadowJar {
    archiveFileName.set("connector.jar")
    mergeServiceFiles()
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    manifest {
        attributes(mapOf("Main-Class" to "org.eclipse.edc.boot.system.runtime.BaseRuntime"))
    }
}

// Make shadowJar the default output
tasks.jar {
    enabled = false
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}
