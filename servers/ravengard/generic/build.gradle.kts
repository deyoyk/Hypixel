plugins {

    java

}



group = "com.deyo.hypixel.ravengard"

version = "3.0"



java {

    sourceCompatibility = JavaVersion.VERSION_25

    targetCompatibility = JavaVersion.VERSION_25

    toolchain {

        languageVersion.set(JavaLanguageVersion.of(25))

    }

}



dependencies {

    implementation(project(":servers:lobby:generic"))

    implementation(project(":platform:commons"))

    implementation(project(":platform:packer"))

    implementation(project(":platform:proxy-api"))

    implementation(libs.mongodb.bson)

    implementation(libs.mongodb.driver.sync)

    compileOnly(libs.minestom) {

        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")

    }

    implementation(libs.gson)

    implementation(libs.tinylog.api)

    implementation(libs.tinylog.impl)



    testImplementation(libs.minestom) {

        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")

    }

}

