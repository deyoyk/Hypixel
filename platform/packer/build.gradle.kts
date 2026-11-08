import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    application
    alias(libs.plugins.shadow)
}

group = "com.deyo.hypixel.platform"
version = "3.0"

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

dependencies {
    api(libs.creative.api)
    api(libs.creative.serializer.minecraft)
    api(libs.creative.server)
}

application {
    mainClass.set("com.deyo.hypixel.packer.HypixelPackServer")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("com.deyo.hypixel.packer.HypixelPackServer")
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}
