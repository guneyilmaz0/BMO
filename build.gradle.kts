plugins {
    kotlin("jvm") version "1.9.23"
}

group = "net.guneyilmaz0.bmo"
version = "1.0.0-build.1"
description = "BMO BOT!"

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.24")
    implementation("dev.arbjerg:lavaplayer:2.2.2")
    implementation("com.github.guneyilmaz0:MongoS:4.2.3")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "net.guneyilmaz0.bmo.Main"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

