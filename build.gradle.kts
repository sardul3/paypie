import org.gradle.internal.impldep.org.junit.platform.launcher.TagFilter.excludeTags
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("java")
    jacoco
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"

    id("info.solidsoft.pitest") version "1.15.0"

}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

group = "io.github.sardul3"
version = "1.0-SNAPSHOT"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.mockito:mockito-junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter:1.20.6")
    testImplementation("org.testcontainers:postgresql:1.20.6")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.2.1")


}

jacoco {
    toolVersion = "0.8.12"
}

tasks.jacocoTestReport {
    reports {
        xml.required = false
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
}

tasks.test {
    useJUnitPlatform() {
        excludeTags("integration")
    }
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.named<BootBuildImage>("bootBuildImage") {
    imageName.set("sagarpoudel14/paypie-app")
    environment.set(
        mapOf(
            "BP_JVM_VERSION" to "17",
            "BP_NATIVE_IMAGE" to "false"
        )
    )
    builder.set("paketobuildpacks/builder:tiny") // or "tiny", "base"
}

pitest {
    junit5PluginVersion.set("1.2.1") // required for JUnit 5
    testPlugin.set("junit5") // if you're using JUnit 5
    targetClasses.set(listOf("io.github.sardul3.expense.domain.*")) // your main classes
    targetTests.set(listOf("io.github.sardul3.expense.expense.*")) // your test classes
    threads.set(Runtime.getRuntime().availableProcessors())
    outputFormats.set(listOf("HTML", "XML"))
    mutationThreshold.set(80) // set threshold for failing builds
    coverageThreshold.set(80) // optional line coverage requirement
    timestampedReports.set(false)
}

repositories {
    mavenCentral()
}