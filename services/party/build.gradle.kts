import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar



plugins {

    java

    application

    alias(libs.plugins.shadow)

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

    implementation(project(":platform:commons"))

    implementation(libs.caffeine)

    implementation(libs.tinylog.api)

    implementation(libs.tinylog.impl)

}



application {

    mainClass.set("com.deyo.hypixel.service.party.PartyService")

}



tasks {

    named<ShadowJar>("shadowJar") {

        archiveBaseName.set("ServiceParty")

        archiveClassifier.set("")

        archiveVersion.set("")

    }

}