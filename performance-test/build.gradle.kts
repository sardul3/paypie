plugins {
    id("java")
    id("io.gatling.gradle") version "3.9.5"
}

group = "io.github.sardul3"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    gatlingImplementation("io.gatling:gatling-http-java:3.13.5")
    gatlingImplementation("io.gatling:gatling-core-java:3.13.5")
    gatlingImplementation("io.gatling:gatling-app:3.13.5")
    gatlingImplementation("io.gatling:gatling-recorder:3.13.5")
    gatlingImplementation("io.gatling.highcharts:gatling-charts-highcharts:3.13.5")
    gatlingImplementation("ch.qos.logback:logback-classic:1.4.14")
}

gatling {
    jvmArgs = listOf(
        "-server",
        "-Xmx1G",
        "-XX:+UseG1GC",
        "-XX:+ParallelRefProcEnabled",
        "-XX:MaxInlineLevel=20",
        "-XX:MaxTrivialSize=12",
        "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED",
        "--add-opens=java.base/java.lang=ALL-UNNAMED"
    )

}

sourceSets {
    test {
        java {
            srcDirs("src/test/java")
        }
        resources {
            srcDirs("src/test/resources")
        }
    }
}