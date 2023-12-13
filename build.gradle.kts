plugins {
    kotlin("jvm") version "1.9.20"
}

sourceSets {
    main {
        resources.setSrcDirs(kotlin.sourceDirectories)
    }
}

dependencies {
    testImplementation(platform("io.kotest:kotest-bom:5.8.0"))
    testImplementation("io.kotest:kotest-assertions-api-jvm")
    testImplementation("io.kotest:kotest-framework-api-jvm")
    testImplementation("io.kotest:kotest-framework-datatest-jvm")
    testImplementation("io.kotest:kotest-property-jvm")
    testRuntimeOnly("io.kotest:kotest-runner-junit5")
}

tasks.test.configure {
    useJUnitPlatform()

    jvmArgs("-Xmx8G")
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}
