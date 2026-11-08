rootProject.name = "Hypixel-v2"

// platform
include(":platform:commons")
include(":platform:server-runtime")
include(":platform:proxy-api")
include(":platform:velocity")
include(":platform:loader")
include(":platform:packer")
include(":platform:configuration")
include(":platform:pvp")
include(":platform:anticheat")
include(":platform:dungeons")
include(":platform:spark")

// services
include(":services:api")
include(":services:generic")
include(":services:auctionhouse")
include(":services:bazaar")
include(":services:darkauction")
include(":services:elections")
include(":services:friend")
include(":services:guild")
include(":services:itemtracker")
include(":services:orchestrator")
include(":services:party")
include(":services:punishment")
include(":services:replay")
include(":services:store")

// servers - skyblock
include(":servers:skyblock:core")
include(":servers:skyblock:hub")
include(":servers:skyblock:island")
include(":servers:skyblock:goldmine")
include(":servers:skyblock:deepcaverns")
include(":servers:skyblock:dwarvenmines")
include(":servers:skyblock:thepark")
include(":servers:skyblock:galatea")
include(":servers:skyblock:backwaterbayou")
include(":servers:skyblock:jerrysworkshop")
include(":servers:skyblock:spidersden")
include(":servers:skyblock:theend")
include(":servers:skyblock:crimsonisle")
include(":servers:skyblock:dungeonhub")
include(":servers:skyblock:thefarmingislands")

// servers - competitive
include(":servers:bedwars:lobby")
include(":servers:bedwars:game")
include(":servers:bedwars:configurator")
include(":servers:skywars:lobby")
include(":servers:skywars:game")
include(":servers:skywars:configurator")
include(":servers:murdermystery:lobby")
include(":servers:murdermystery:game")
include(":servers:murdermystery:configurator")

// servers - ravengard + lobby
include(":servers:ravengard:generic")
include(":servers:ravengard:lobby")
include(":servers:ravengard:dungeon")
include(":servers:lobby:lobby")
include(":servers:lobby:mainlobby")
include(":servers:lobby:prototypelobby")
include(":servers:lobby:generic")
include(":servers:lobby:game")
include(":servers:lobby:replayviewer")

val clientModRequested = gradle.startParameter.taskNames.any { taskName ->
    taskName == "clientmod" ||
        taskName == ":clientmod" ||
        taskName.startsWith("clientmod:") ||
        taskName.startsWith(":clientmod:")
}

if (clientModRequested || gradle.startParameter.projectProperties.containsKey("includeClientmod")) {
    include(":platform:clientmod")
}

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        gradlePluginPortal()
    }
}
