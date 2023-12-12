plugins {
    kotlin("jvm") version "1.9.20"
}

sourceSets {
    main {
        resources.setSrcDirs(kotlin.sourceDirectories)
    }
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}
