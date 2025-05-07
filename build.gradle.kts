import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    jacoco
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    id("info.solidsoft.pitest") version "1.15.0"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

group = "io.github.sardul3"
version = "0.0.1"

repositories {
    mavenCentral()
}

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
    dependsOn(tasks.test) // Ensures test runs before report
    reports {
        xml.required.set(true)
        csv.required.set(true)
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform {
        excludeTags("integration")
    }

    maxHeapSize = "1G"

    testLogging {
        events = setOf(TestLogEvent.PASSED)
    }

    finalizedBy(tasks.jacocoTestReport)
}

tasks.named<BootBuildImage>("bootBuildImage") {
    imageName.set("sagarpoudel14/paypie-app")
    environment.set(
        mapOf(
            "BP_JVM_VERSION" to "17",
            "BP_NATIVE_IMAGE" to "false"
        )
    )
    builder.set("paketobuildpacks/builder:tiny")
}

pitest {
    junit5PluginVersion.set("1.2.1")
    targetClasses.set(listOf("io.github.sardul3.expense.domain.model.*"))
    targetTests.set(listOf("io.github.sardul3.expense.expense.model.*"))
    threads.set(Runtime.getRuntime().availableProcessors())
    outputFormats.set(listOf("HTML", "XML"))
    mutationThreshold.set(80)
    coverageThreshold.set(80)
    timestampedReports.set(false)
//    jvmArgs.set(
//        listOf(
//            "--add-opens=java.base/java.lang=ALL-UNNAMED",
//            "--add-opens=java.base/java.util=ALL-UNNAMED"
//        )
//    )
}

tasks.register<Test>("integrationTest") {
    useJUnitPlatform {
        includeTags("integration")
    }

    shouldRunAfter(tasks.test)

    testLogging {
        events = setOf(TestLogEvent.PASSED)
    }

    description = "Runs only integration tests marked with @Tag(\"integration\")"
    group = "verification"
}
