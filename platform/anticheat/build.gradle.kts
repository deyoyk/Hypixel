plugins {
    id("java")
    id("maven-publish")
}

group = "com.deyo.hypixel.platform"
version = "1.0"

repositories {
    mavenCentral()

    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/deyo/Hypixel-v2")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USERNAME")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    compileOnly(libs.minestom) {
        exclude(group = "org.jboss.shrinkwrap.resolver", module = "shrinkwrap-resolver-depchain")
    }
    implementation(libs.tinylog.api)
    implementation(libs.tinylog.impl)

    compileOnly(libs.protocollib)

    testImplementation("org.junit.jupiter:junit-jupiter:6.1.3")
    testImplementation(libs.minestom)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/deyo/Hypixel-v2")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            groupId = "com.deyo.hypixel"
            artifactId = "anticheat"
            version = "1.0"

            from(components["java"])
        }
    }
}