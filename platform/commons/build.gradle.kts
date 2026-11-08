plugins {

    `java-library`

    id("maven-publish")

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

    implementation(libs.json)

    implementation(libs.snakeyaml)

    implementation(project(":platform:packer"))

    implementation(libs.mongodb.bson)

    implementation(libs.tinylog.api)

    implementation(libs.tinylog.impl)

    implementation(libs.zstd.jni)

    api(libs.adventure.api)



    compileOnly(libs.minestom) {

        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")

    }



    // Must match deyo-redisapi's Jedis version to avoid conflicts

    implementation(libs.jedis)

    implementation(libs.deyo.redisapi)

    api(libs.deyo.datahandler)



    implementation(libs.configlib.yaml)



    implementation(libs.configurate.yaml)

    implementation(libs.javapoet)



    testImplementation("org.junit.jupiter:junit-jupiter:6.1.3")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}



tasks.test {

    useJUnitPlatform()

}



sourceSets {

    val main by getting



    val codegen by creating {

        java.srcDir("src/codegen/java")



        compileClasspath += main.compileClasspath

        runtimeClasspath += main.compileClasspath

    }



    codegen // just here to avoid "unused" warning

}



val generateItemTypes by tasks.registering(JavaExec::class) {

    group = "codegen"

    description = "Generate ItemType enum from SkyBlock YAML"



    classpath = sourceSets["codegen"].runtimeClasspath

    mainClass.set("com.deyo.hypixel.codegen.ItemTypeGenerator")



    args(

        rootProject.projectDir

            .resolve("config/game/skyblock/items")

            .absolutePath,

        layout.projectDirectory

            .dir("src/generated/java")

            .asFile

            .absolutePath

    )



    inputs.dir(rootProject.projectDir.resolve("config/game/skyblock/items"))

    outputs.dir(layout.projectDirectory.dir("src/generated/java"))

}



sourceSets["main"].java {

    srcDir(layout.projectDirectory.dir("src/generated/java"))

}



tasks.compileJava {

    dependsOn(generateItemTypes)

}

