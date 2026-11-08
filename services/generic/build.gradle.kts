plugins {

    java

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

    mavenCentral()

    maven("https://jitpack.io")

}



dependencies {

    implementation(project(":platform:commons"))

    implementation(libs.deyo.redisapi)

    implementation(libs.adventure.api)

    implementation(libs.adventure.text.serializer.gson)

    implementation(libs.tinylog.api)

}