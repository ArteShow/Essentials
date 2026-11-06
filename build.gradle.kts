
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

val paperVersion = providers.gradleProperty("paperVersion")
    .getOrElse("26.1.2")

val javaRelease = providers.gradleProperty("javaRelease")
    .map { it.toInt() }
    .getOrElse(25)

dependencies {
    compileOnly("io.papermc.paper:paper-api:$paperVersion")
    compileOnly("org.mvplugins.multiverse.core:multiverse-core:5.8.2")
    compileOnly("org.xerial:sqlite-jdbc:3.54.1.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(javaRelease)
    }

    jar {
        archiveBaseName.set("Essentials")
    }
}
