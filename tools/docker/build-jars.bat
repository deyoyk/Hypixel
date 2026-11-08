@echo off
setlocal
pushd "%~dp0..\.."
set "ROOT=%CD%"
popd
docker ps -q --filter "name=hypixel-gradle-build" | findstr . >nul && (echo Build already running, wait for it to finish & exit /b 1)
docker run --rm --name hypixel-gradle-build -v "%ROOT%:/work" -w /work -v hypixel-gradle-cache:/root/.gradle eclipse-temurin:25-jdk bash -lc "sed -i 's/\r$//' gradlew && chmod +x gradlew && ./gradlew shadowJar -x test --console=plain && for jar in HypixelCore ServiceAPI ServiceAuctionHouse ServiceBazaar ServiceItemTracker ServiceParty ServiceDarkAuction ServiceOrchestrator ServiceFriend ServiceStore ServiceElection ServiceGuild ServicePunishment ServiceReplay com.deyo.hypixel.packer.HypixelPackServer SkyBlockProxy SkyBlockSpark; do f=$(find /work -path *build/libs/$jar.jar | head -n 1); [ -z $f ] && { echo missing $jar; exit 1; }; cp $f /work/tools/docker/jars/$jar.jar; done && ls /work/tools/docker/jars" || exit /b 1
echo Jars ready in tools\docker\jars
