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

    implementation(project(":platform:commons"))

    implementation(libs.caffeine)

    implementation(libs.tinylog.api)

    implementation(libs.tinylog.impl)

    implementation(libs.mongodb.bson)

    implementation(libs.mongodb.driver.sync)

    implementation(libs.annotations)

    annotationProcessor(libs.annotations)

}



application {

    mainClass.set("com.deyo.hypixel.service.guild.GuildService")

}



tasks {

    named<ShadowJar>("shadowJar") {

        archiveBaseName.set("ServiceGuild")

        archiveClassifier.set("")

        archiveVersion.set("")

    }

}

