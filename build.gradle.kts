plugins {
    java
}

group = "dev.arteshow"
version = property("pluginVersion") as String

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc"
    }
}

dependencies {
    compileOnly(
        "io.papermc.paper:paper-api:${property("paperVersion")}"
    )
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(25)
    }

    jar {
        archiveBaseName.set("Essentials")
    }
}