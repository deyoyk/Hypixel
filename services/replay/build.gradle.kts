import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar



plugins {

    java

    application

    id("com.gradleup.shadow") version "9.6.1"

}



group = "com.deyo.hypixel.services"

version = "3.0"



java {

    sourceCompatibility = JavaVersion.VERSION_25

    targetCompatibility = JavaVersion.VERSION_25

    toolchain {

        languageVersion.set(JavaLanguageVersion.of(25))

    }

}



repositories {

    maven("https://jitpack.io")

    mavenCentral()

}



dependencies {

    implementation(project(":services:generic"))

    implementation(project(":servers:lobby:game"))

    implementation(project(":platform:commons"))

    implementation(libs.caffeine)

    implementation(libs.tinylog.api)

    implementation(libs.tinylog.impl)

    implementation(libs.gson)

    implementation(libs.mongodb.bson)

    implementation(libs.mongodb.driver.sync)



}



application {

    mainClass.set("com.deyo.hypixel.service.replay.ReplayService")

}



tasks {

    named<ShadowJar>("shadowJar") {

        archiveBaseName.set("ServiceReplay")

        archiveClassifier.set("")

        archiveVersion.set("")

    }

}

