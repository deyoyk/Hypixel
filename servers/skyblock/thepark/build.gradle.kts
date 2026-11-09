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

    implementation(libs.tinylog.api)

    implementation(libs.tinylog.impl)

    implementation(libs.joml)

    testImplementation("org.junit.jupiter:junit-jupiter:6.1.3")
    testImplementation(libs.minestom)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}