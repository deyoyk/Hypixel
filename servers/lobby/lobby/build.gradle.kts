plugins {

    java

}



group = "com.deyo.hypixel.lobby"

version = "1.0"



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

}



dependencies {

    implementation(project(":platform:commons"))

    implementation(project(":platform:proxy-api"))

    implementation(project(":servers:lobby:generic"))

    implementation(libs.mongodb.bson)

    implementation(libs.tinylog.api)

    implementation(libs.tinylog.impl)

    implementation(libs.minestom) {

        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")

    }

    implementation(libs.adventure.text.minimessage)

}

