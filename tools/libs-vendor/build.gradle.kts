import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.artifacts.ModuleDependency

// Vendoring bootstrap: relocates third-party bytecode under
// com.deyo.hypixel.libs so project sources carry zero upstream branding.
// Produces:
//   com.deyo.hypixel.libs:deyo-datahandler:1.6.0
//   com.deyo.hypixel.libs:deyo-redisapi:1.1.6
// Inputs are deliberately NOT hardcoded here; supply them per machine via
//   tools/libs-vendor/local.properties (see local.properties.example, gitignored)
// or -P flags, then run: gradle -p tools/libs-vendor relocateAll
plugins {
    id("com.gradleup.shadow") version "9.6.1"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

val localProps = java.util.Properties().apply {
    rootProject.layout.projectDirectory.file("local.properties").asFile
        .takeIf { it.exists() }
        ?.inputStream()?.use(::load)
}

fun vendorProp(key: String): String =
    findProperty(key) as String?
        ?: localProps.getProperty(key)
        ?: error("Missing required vendoring input '$key' (see local.properties.example)")

fun pomFor(group: String, artifact: String, version: String, deps: List<Triple<String, String, String>>): String {
    val depsXml = deps.joinToString("") { (g, a, v) ->
        "<dependency><groupId>$g</groupId><artifactId>$a</artifactId><version>$v</version><scope>runtime</scope></dependency>"
    }
    return """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
  <modelVersion>4.0.0</modelVersion>
  <groupId>$group</groupId><artifactId>$artifact</artifactId><version>$version</version>
  <dependencies>$depsXml</dependencies>
</project>
"""
}

fun relocateLib(
    task: String,
    conf: String,
    coordsKey: String,
    prefixKey: String,
    base: String,
    ver: String,
    pomDeps: List<Triple<String, String, String>>
) {
    val vendorConf = configurations.create(conf)
    val dep = dependencies.create(vendorProp(coordsKey)) as ModuleDependency
    dep.isTransitive = false
    dependencies.add(conf, dep)
    tasks.register<ShadowJar>(task) {
        configurations = listOf(vendorConf)
        relocate(vendorProp(prefixKey), "com.deyo.hypixel.libs")
        archiveBaseName.set(base)
        archiveVersion.set(ver)
        archiveClassifier.set("")
        val outDir = rootProject.layout.projectDirectory.dir("../maven-repo/com/deyo/hypixel/libs/$base/$ver")
        destinationDirectory.set(outDir)
        doLast {
            outDir.asFile.resolve("$base-$ver.pom").writeText(
                pomFor("com.deyo.hypixel.libs", base, ver, pomDeps)
            )
        }
    }
}

relocateLib(
    "relocateDatahandler", "vendorDatahandler",
    "vendor.datahandler.coords", "vendor.datahandler.prefix",
    "deyo-datahandler", "1.6.0",
    listOf(Triple("com.google.code.gson", "gson", "2.11.0"))
)
relocateLib(
    "relocateRedisapi", "vendorRedisapi",
    "vendor.redisapi.coords", "vendor.redisapi.prefix",
    "deyo-redisapi", "1.1.6",
    listOf(Triple("redis.clients", "jedis", "7.2.0"))
)
tasks.register("relocateAll") {
    dependsOn("relocateDatahandler", "relocateRedisapi")
}
