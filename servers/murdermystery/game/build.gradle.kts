plugins {

    java

}



group = "com.deyo.hypixel.murdermystery"

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

    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")

    maven("https://repo.viaversion.com")

}



dependencies {

    implementation(project(":servers:lobby:generic"))

    implementation(project(":platform:commons"))

    implementation(project(":platform:proxy-api"))

    implementation(project(":platform:pvp"))

    implementation(libs.mongodb.bson)

    implementation(libs.adventure.text.minimessage)

    implementation(libs.polar)

    implementation(libs.fastutil)

    compileOnly(libs.minestom) {

        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")

    }

    implementation(libs.tinylog.api)

    implementation(libs.tinylog.impl)

}

