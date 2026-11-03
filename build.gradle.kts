plugins {
    java
}

group = "dev.arteshow"
version = property("pluginVersion") as String

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.onarandombox.com/content/groups/public/")
}

dependencies {
    compileOnly(
        "io.papermc.paper:paper-api:${property("paperVersion")}"
    )

    compileOnly("org.mvplugins.multiverse.core:multiverse-core:5.8.2")
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