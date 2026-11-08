import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar



plugins {

    java

    application

    alias(libs.plugins.shadow)

}



group = "com.deyo.hypixel.platform"

version = "3.0"



java {

    toolchain {

        languageVersion.set(JavaLanguageVersion.of(25))

    }

}



dependencies {

    implementation(libs.minestom) {

        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")

    }

    implementation(project(":servers:lobby:generic"))

    implementation(libs.guava)

    implementation(libs.protobuf.javalite)

    api(libs.spark.common)

}



application {

    mainClass.set("com.deyo.hypixel.spark.Spark")

}



tasks.withType<ShadowJar> {

    archiveBaseName.set("SkyBlockSpark")

    archiveClassifier.set("")

    archiveVersion.set("")

}