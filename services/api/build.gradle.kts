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

    implementation(libs.caffeine)

    implementation(project(":services:generic"))

    implementation(project(":servers:lobby:generic"))

    implementation(project(":platform:commons"))

    implementation(libs.gson)

    implementation(libs.spark.core)

    implementation(libs.mongodb.bson)

    implementation(libs.mongodb.driver.sync)

    implementation(libs.tinylog.api)

    implementation(libs.tinylog.impl)

    compileOnly(libs.annotations)

    testImplementation("org.junit.jupiter:junit-jupiter:6.1.3")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

tasks.test {

    useJUnitPlatform()

}



application {

    mainClass.set("com.deyo.hypixel.service.api.APIService")

}



tasks {

    named<ShadowJar>("shadowJar") {

        archiveBaseName.set("ServiceAPI")

        archiveClassifier.set("")

        archiveVersion.set("")

    }

}