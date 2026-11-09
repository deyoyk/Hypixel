plugins {

    java

}



group = "com.deyo.hypixel.skyblock"

version = "3.0"



java {

    sourceCompatibility = JavaVersion.VERSION_25

    targetCompatibility = JavaVersion.VERSION_25

    toolchain {

        languageVersion.set(JavaLanguageVersion.of(25))

    }

}



dependencies {

    implementation(project(":servers:skyblock:core"))

    implementation(project(":servers:lobby:generic"))

    implementation(project(":platform:commons"))

    implementation(project(":platform:proxy-api"))

    compileOnly(libs.minestom) {

        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")

    }

    testImplementation(libs.minestom)

    implementation(libs.tinylog.api)

    implementation(libs.tinylog.impl)

    implementation(libs.mongodb.bson)

}